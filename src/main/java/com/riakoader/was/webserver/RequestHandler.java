package com.riakoader.was.webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Strings;
import com.riakoader.was.handler.HandlerMethod;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.util.HttpRequestUtils;
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

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = receiveRequest(in);

            HandlerMethod handlerMethod = HandlerMethodMapper.getHandlerMethod(request.getRequestURI());
            HttpResponse response = handlerMethod.service(request);

            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     *
     * @param in
     * @return HttpRequest
     * @throws IOException
     */
    private HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        log.debug("request line : {}", line);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.add(pair);

            log.debug("header : {}", line);
        }

        return new HttpRequest(requestLine, headers);
    }

    /**
     *
     * @param out
     * @param response
     */
    private void sendResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.writeBytes(response.getStatusLine() + System.lineSeparator());
            dos.writeBytes(response.getHeader("Content-Type") + System.lineSeparator());
            dos.writeBytes(response.getHeader("Content-Length") + System.lineSeparator());
            dos.writeBytes(System.lineSeparator());
            dos.write(response.getBody());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
