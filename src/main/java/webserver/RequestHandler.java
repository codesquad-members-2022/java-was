package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.RequestMapping;
import http.HttpStatus;
import http.Response;
import util.HttpRequestUtils;
import webserver.dto.RequestLine;

public class RequestHandler extends Thread {
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

            RequestLine requestLine = RequestLine.of(br.readLine());

            processRequest(br, out, requestLine);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void processRequest(BufferedReader br, OutputStream out, RequestLine requestLine) throws Exception {
        String contentType = HttpRequestUtils.extractContentType(requestLine.getUrl());

        printHeaders(br);

        if (RequestMapping.contains(requestLine.getHttpMethod(), requestLine.getUrl())) {
            processDynamicRequest(out, requestLine, contentType);
            return;
        }
        processStaticRequest(out, requestLine, contentType);
    }

    private void processDynamicRequest(OutputStream out, RequestLine requestLine, String contentType) throws Exception {
        Dispatcher dispatcher = Dispatcher.getInstance();
        Response response = dispatcher.handleRequest(requestLine);

        response(out, new byte[] {}, contentType, response.getHttpStatus());
    }

    private void processStaticRequest(OutputStream out, RequestLine requestLine, String contentType) throws
        IOException {
        StaticResourceProcessor staticResourceProcessor = StaticResourceProcessor.getInstance();
        byte[] body = staticResourceProcessor.readStaticResource(requestLine.getUrl());
        response(out, body, contentType, HttpStatus.OK);
    }

    private void printHeaders(BufferedReader br) throws IOException {
        String header;
        while (((header = br.readLine()) != null) && header.equals("")) {
            log.debug("header : {}", header);
        }
    }

    private void response(OutputStream out, byte[] body, String contentType, HttpStatus httpStatus) {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader(dos, body.length, contentType, httpStatus);
        responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, String contentType,
        HttpStatus httpStatus) {
        try {
            dos.writeBytes(String.format("HTTP/1.1 %d %s \r\n", httpStatus.getStatusCode(), httpStatus.name()));
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
