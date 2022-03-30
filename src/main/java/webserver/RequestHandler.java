package webserver;

import db.SessionDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import webserver.controller.UrlMapper;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest request = generateHttpRequest(new BufferedReader(new InputStreamReader(in)));
            HttpResponse response = UrlMapper.getResponse(request);
            renderView(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpRequest generateHttpRequest(BufferedReader br) throws IOException {
        String[] tokens = readRequestLine(br);

        String method = tokens[0];
        String url = tokens[1];
        String version = tokens[2];

        Map<String, String> requestHeader = readRequestHeader(br);
        boolean isLogin = isValidCookie(requestHeader);
        log.debug("isLogin : {}", isLogin);

        String requestBody = null;

        if (requestHeader.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
            requestBody = IOUtils.readData(br, contentLength);

            log.debug("Body: {}", requestBody);
            log.debug("Content-Length: {}", requestBody.length());
        }

        return new HttpRequest(method, url, version, requestHeader, requestBody, isLogin);
    }

    private boolean isValidCookie(Map<String, String> requestHeader) {
        if (requestHeader.containsKey("Cookie")) {
            String cookie = requestHeader.get("Cookie");
            Map<String, String> cookieMap = HttpRequestUtils.parseCookies(cookie);
            log.debug("cookieMap : {}", cookieMap.toString());
            String sessionId = cookieMap.get("sessionId");
            return SessionDataBase.isLoginUser(sessionId);
        }
        return false;
    }

    private String[] readRequestLine(BufferedReader br) throws IOException {
        String l = br.readLine();
        log.debug("Request Line: {}", l);
        String line = URLDecoder.decode(l, StandardCharsets.UTF_8);
        return line.split(" ");
    }

    private Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
        String line;
        Map<String, String> requestHeaderMap = new HashMap<>();
        while (!(line = br.readLine()).isBlank()) {
            Pair pair = HttpRequestUtils.parseHeader(line);
            requestHeaderMap.put(pair.getKey(), pair.getValue());
        }
        return requestHeaderMap;
    }

    private void renderView(DataOutputStream dos, HttpResponse response) throws IOException {
        if (response != null) {
            writeResponse(dos, response);
        }
    }

    private void writeResponse(DataOutputStream dos, HttpResponse response) {
        writeResponseLine(dos,response);
        writeResponseHeaders(dos, response);
        writeResponseBody(dos, response.getResponseBody());
    }

    private void writeResponseLine(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(String.format("%s %d %s %s",
                    response.getVersion(), response.getHttpStatusCode(), response.getHttpStatusMessage(), System.lineSeparator()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeResponseHeaders(DataOutputStream dos, HttpResponse response) {
        try {
            Map<String, String> responseHeaders = response.getResponseHeaders();
            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                dos.writeBytes(String.format("%s: %s %s", entry.getKey(), entry.getValue(), System.lineSeparator()));
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
