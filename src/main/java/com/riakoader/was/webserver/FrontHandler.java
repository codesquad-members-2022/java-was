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

            sendResponse(out, assign(receiveRequest(in)));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private HttpResponse assign(HttpRequest request) throws IOException {
        String uri = request.getRequestURI();
        int lastIndex = request.getRequestURI().lastIndexOf("/");
        String lastPath = uri.substring(lastIndex + 1);

        String pattern = "^.*\\..*$";
        if (Pattern.matches(pattern, lastPath)) {
            return resourceHandler.service(request);
        }

        Handler handler = handlerMapper.getHandler(getCurrentPath(request.getRequestURI(), 1));
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
