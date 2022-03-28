package com.riakoader.was.webserver;

import java.io.*;
import java.net.Socket;

import com.riakoader.was.handler.HandlerMethod;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.riakoader.was.util.HttpRequestUtils.receiveRequest;
import static com.riakoader.was.util.HttpResponseUtils.sendResponse;

public class RequestHandler extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    /**
     * 1. 'receiveRequest' 메서드를 사용하여 클라이언트가 보낸 데이터 스트림을 읽어 요청 객체로 변환한다.
     * 2. 'HandlerMethodMapper' 로 요청 URI 값으로 매핑된 'HandlerMethod' 를 찾아 해당 요청을 수행하도록 한다.
     * 3. 'HandlerMethod' 가 반환한 결과 값을 받아 클라이언트에게 응답한다.
     */
    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            HttpRequest request = receiveRequest(in);

            HandlerMethod handlerMethod = HandlerMethodMapper.getHandlerMethod(
                    new Pair<>(
                            request.getMethod(),
                            request.getRequestURI()
                    )
            );
            HttpResponse response = handlerMethod.service(request);
            sendResponse(out, response);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
