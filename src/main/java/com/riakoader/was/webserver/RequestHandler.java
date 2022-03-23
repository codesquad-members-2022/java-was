package com.riakoader.was.webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.util.HttpRequestUtils;
import com.riakoader.was.util.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            HttpRequest request = receiveRequest(in);
            HttpResponse response = createResponse(request);

            sendResponse(out, response);
            
//            User user = new User(request.getParameter("userId"), request.getParameter("password"),
//                request.getParameter("name"), request.getParameter("email"));

//            response200Header(dos, body.length);
//            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.write(response.toByteArray());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpResponse createResponse(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse();
        response.setStatusLine(request.getProtocol(), HttpStatus.OK);
        response.setHeader("Content-Type", "text/html;charset=utf-8");

        byte[] body = Files.readAllBytes(new File(Path.WEBAPP_PATH + request.getRequestURI()).toPath());
        response.setHeader("Content-Length", String.valueOf(body.length));
        response.setBody(body);

        return response;
    }

    private HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        log.debug("request line : {}", line);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
            headers.add(HttpRequestUtils.parseHeader(line));

            log.debug("header : {}", line);
        }

        return new HttpRequest(requestLine, headers);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
