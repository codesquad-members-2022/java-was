package com.riakoader.was.handler;

import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.util.Pair;

import java.io.IOException;

public class Handler {

    private static volatile Handler handler;

    private final HandlerMethodMapper handlerMethodMapper = HandlerMethodMapper.getInstance();

    private Handler() {
    }

    public static Handler getInstance() {
        if (handler == null) {
            handler = new Handler();
        }
        return handler;
    }

    /**
     * 1. 'HandlerMethodMapper' 로 요청 URI 값으로 매핑된 'HandlerMethod' 를 찾아 해당 요청을 수행하도록 한다.
     * 2. 'HandlerMethod' 가 반환한 결과 값을 받아 FrontHandler 에게 전달한다.
     */
    public HttpResponse service(HttpRequest request) throws IOException {
        HandlerMethod handlerMethod = handlerMethodMapper.getHandlerMethod(
                new Pair<>(
                        request.getMethod(),
                        request.getRequestURI()
                )
        );
        return handlerMethod.service(request);
    }
}
