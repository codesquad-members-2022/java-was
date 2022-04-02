package com.riakoader.was.webserver;

import com.google.common.base.Strings;
import com.riakoader.was.handler.Handler;
import com.riakoader.was.handler.HandlerMapper;
import com.riakoader.was.handler.ResourceHandler;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.util.HttpRequestUtils;
import com.riakoader.was.util.IOUtils;
import com.riakoader.was.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import static com.riakoader.was.util.HttpRequestUtils.getCurrentPath;

public class FrontHandler {

    private static final Logger logger = LoggerFactory.getLogger(FrontHandler.class);

    private static volatile FrontHandler frontHandler;

    private final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private final ResourceHandler resourceHandler = ResourceHandler.getInstance();

    private final HandlerMapper handlerMapper = HandlerMapper.getInstance();

    private FrontHandler() {
    }

    public static FrontHandler getInstance() {
        if (frontHandler == null) {
            synchronized (FrontHandler.class) {
                if (frontHandler == null) {
                    frontHandler = new FrontHandler();
                }
            }
        }
        return frontHandler;
    }

    public void process(Socket connection) {
        CompletableFuture.runAsync(() -> run(connection), executor);
    }

    private void run(Socket connection) {
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {

            /*
             * 1. receiveRequest(in) - InputStream 에서 클라이언트가 보낸 요청을 읽어들여 HttpRequest 객체를 생성합니다.
             * 2. assign(receiveRequest(in)) - receiveRequest 메서드가 반환한 HttpRequest 객체 (클라이언트 요청 정보) 를 담당 Handler 에게 위임합니다. (URL 매핑)
             * 3. sendResponse(out, assign(receiveRequest(in))) - assign 메서드가 반환한 (담당 Handler 가 클라이언트 요청을 받아 생성한 응답) HttpResponse 객체를 OutputStream 을 사용하여 클라이언트에게 응답합니다.
             *
             */
            sendResponse(out, assign(receiveRequest(in)));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse assign(HttpRequest request) throws IOException {
        String uri = request.getRequestURI();
        int lastIndex = request.getRequestURI().lastIndexOf("/");
        String lastPath = uri.substring(lastIndex + 1);

        /**
         * .css, .js, .xx 등 확장자가 붙은 파일이 요청 URI 의 '마지막 경로' 일 경우
         */
        String pattern = "^.*\\..*$";
        if (Pattern.matches(pattern, lastPath)) {
            return resourceHandler.service(request);
        }

        /*
         * getCurrentPath(request.getRequestURI(), Handler.depth)) - 요청 URI 에서 현재 depth 에 따라 매핑해야할 경로를 얻습니다. (Handler - depth 1, HandlerMapper - depth 2)
         * ex. /users/create - Handler: /users, HandlerMapper: /create
         * Handler 초기 생성 시 '필드' 로 매핑된 URL 을 설정해놓고 싶었지만, 시간 관계상 가장 단순하게 순차적으로 잘라 매핑하는 방식으로 구현했습니다.
         *
         */
        Handler handler = handlerMapper.getHandler(getCurrentPath(request.getRequestURI(), Handler.DEPTH));
        return handler.service(request);
    }

    /**
     * 클라이언트가 보낸 데이터 스트림을 'RequestLine', 'RequestHeaders', (+ RequestMessageBody) 로 구분 지어 읽어들인다.
     * 읽어들인 메시지들을 사용하여 HttpRequest 객체를 생성하고 이를 반환한다.
     *
     * @param in
     * @return 'InputStream' 에서 읽어온 데이터로 HttpRequest 객체를 생성하여 반환한다.
     * @throws IOException
     */
    public HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        logger.debug("request line : {}", line);

        Map<String, String> headers = new HashMap<>();

        while (true) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

            if (Strings.isNullOrEmpty(line)) {
                break;
            }

            Pair<String, String> pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());

            logger.debug("header : {}", line);
        }

        String requestMessageBody = URLDecoder.decode(IOUtils.readData(br, getContentLength(headers)), StandardCharsets.UTF_8);

        return new HttpRequest(requestLine, headers, requestMessageBody);
    }

    private int getContentLength(Map<String, String> headers) {
        return Integer.parseInt(Optional.ofNullable(headers.get("Content-Length")).orElse(String.valueOf(0)));
    }

    /**
     * 매칭시킨 'HandlerMethod' 가 반환한 결과 값을 OutputStream 을 통해 클라이언트에게 응답한다.
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
