package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.RequestMapping;
import http.HttpMethod;
import http.HttpStatus;
import http.Response;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

public class RequestHandler extends Thread {

    private static final String CONTENT_LENGTH = "Content-Length";

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {

            HttpRequestData requestData = new HttpRequestData();

            String requestLine = br.readLine();
            requestData.setHttpRequestLine(HttpRequestUtils.parseHttpRequestLine(requestLine));
            requestData.setHeader(readHeaders(br));

            if (requestLine.contains(HttpMethod.POST.name())) {
                Map<String, String> headers = requestData.getHeader();

                int length = Integer.parseInt(headers.get(CONTENT_LENGTH));
                String decodedRequestBody = URLDecoder.decode(IOUtils.readData(br, length), StandardCharsets.UTF_8);
                Map<String, String> requestBody = HttpRequestUtils.parseQueryString(decodedRequestBody);
                requestData.setRequestBody(requestBody);
            }
            processRequest(out, requestData);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void processRequest(OutputStream out, HttpRequestData requestData) throws Exception {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();

        String contentType = HttpRequestUtils.extractContentType(httpRequestLine.getUrl());

        printHeaders(requestData.getHeader());

        if (RequestMapping.contains(httpRequestLine.getUrl())) {
            processDynamicRequest(out, requestData, contentType);
            return;
        }
        processStaticRequest(out, requestData.getHttpRequestLine(), contentType);
    }

    private void processDynamicRequest(OutputStream out, HttpRequestData requestData, String contentType) throws
        Exception {
        Dispatcher dispatcher = Dispatcher.getInstance();
        Response response = dispatcher.handleRequest(requestData);

        dynamicResponse(out, response);
    }

    private void dynamicResponse(OutputStream out, Response response) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        Map<String, String> headers = new HashMap<>();

        if (response.getHttpStatus().equals(HttpStatus.FOUND)) {
            headers.put("Location", response.getRedirectUrl());
        }

        responseHeader(dos, response.getHttpStatus(), headers);
        flush(dos);
    }

    private void processStaticRequest(OutputStream out, HttpRequestLine requestLine, String contentType) throws
        IOException {
        StaticResourceProcessor staticResourceProcessor = StaticResourceProcessor.getInstance();
        byte[] body = staticResourceProcessor.readStaticResource(requestLine.getUrl());
        staticResponse(out, body, contentType, HttpStatus.OK);
    }

    private void printHeaders(Map<String, String> headers) {
        for (String key : headers.keySet()) {
            log.debug("header : {}: {}", key, headers.get(key));
        }
    }

    private Map<String, String> readHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        Pair pair;
        while (((line = br.readLine()) != null) && !line.equals("")) {
            pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());
        }
        return headers;
    }

    private void staticResponse(OutputStream out, byte[] body, String contentType, HttpStatus httpStatus) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", contentType + ";charset=utf-8");
        headers.put("Content-Length", String.valueOf(body.length));

        responseHeader(dos, httpStatus, headers);
        responseBody(dos, body);
        flush(dos);
    }

    private void responseHeader(DataOutputStream dos, HttpStatus httpStatus, Map<String, String> headers) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", httpStatus.getStatusCode(), httpStatus.name()));
            for (String key : headers.keySet()) {
                dos.writeBytes(key + ": " + headers.get(key) +"\r\n");
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void flush(DataOutputStream dos) throws IOException {
        dos.flush();
    }
}
