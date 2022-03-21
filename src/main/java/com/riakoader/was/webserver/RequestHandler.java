package com.riakoader.was.webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.riakoader.was.httpmessage.HttpClientRequest;
import com.riakoader.was.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.io.File.separatorChar;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP_PATH = "." + separatorChar + "webapp" + separatorChar;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpClientRequest httpClientRequest = receiveRequest(in);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File(WEBAPP_PATH + httpClientRequest.getRequestURI()).toPath());

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private HttpClientRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        String requestLine = line;

        log.debug("request line : {}", line);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = br.readLine();
            headers.add(HttpRequestUtils.parseHeader(line));

            log.debug("header : {}", line);
        }

        return new HttpClientRequest(requestLine, headers);
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
