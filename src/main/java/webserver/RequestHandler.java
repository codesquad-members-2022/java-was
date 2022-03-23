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

    private static final String staticResourcePath = System.getProperty("user.dir") + "/webapp";
    private static final int REQUEST_URI_INDEX = 1;

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

    private void response200(OutputStream out, byte[] body, String contentType) {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    private String extractContentType(String requestUrl) {
        String[] split = requestUrl.split("\\.");
        return ProvidedExtension.extensionResolver(split[split.length - 1]);
    }

    private void response(OutputStream out, byte[] body, String contentType, HttpStatus httpStatus) {
        DataOutputStream dos = new DataOutputStream(out);
        responseHeader(dos, body.length, contentType, httpStatus);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
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
