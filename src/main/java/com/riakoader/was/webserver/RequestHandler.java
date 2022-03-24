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

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    /**
     * 1. `receiveRequest` 메서드를 사용하여 클라이언트가 보낸 데이터 스트림을 읽어 요청 객체로 변환한다.
     * 2. `HandlerMethodMapper` 로 요청 URI 값으로 매핑된 `HandlerMethod` 를 찾아 해당 요청을 수행하도록 한다.
     * 3. `HandlerMethod` 가 반환한 결과 값을 받아 클라이언트에게 응답한다.
     *
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = receiveRequest(in);

            HandlerMethod handlerMethod = HandlerMethodMapper.getHandlerMethod(request.getRequestURI());
            HttpResponse response = handlerMethod.service(request);

            sendResponse(out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * 클라이언트가 보낸 데이터 스트림을 `RequestLine`, `RequestHeaders`, (+ RequestMessageBody) 로 구분 지어 읽어들인다.
     * 읽어들인 메시지들을 사용하여 HttpRequest 객체를 생성하고 이를 반환한다.
     *
     * @param in
     * @return `InputStream` 에서 읽어온 데이터로 HttpRequest 객체를 생성하여 반환한다.
     * @throws IOException
     */
    private HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        logger.debug("request line : {}", line);

        List<HttpRequestUtils.Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(line)) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.add(pair);

            logger.debug("header : {}", line);
        }

        return new HttpRequest(requestLine, headers);
    }

    /**
     * 매칭시킨 `HandlerMethod` 가 반환한 결과 값을 OutputStream 을 통해 클라이언트에게 응답한다.
     *
     * @param out
     * @param response
     */
    private void sendResponse(OutputStream out, HttpResponse response) {
        DataOutputStream dos = new DataOutputStream(out);
        try {
            dos.write(response.toByteArray());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
