package com.riakoader.was.handler;

import com.riakoader.was.handlermethod.HandlerMethod;
import com.riakoader.was.handlermethod.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.util.Pair;

import java.io.IOException;

import static com.riakoader.was.util.HttpRequestUtils.getCurrentPath;

public class UserHandler implements Handler {

    private static volatile UserHandler handler;

    private HandlerMethodMapper handlerMethodMapper;

    private UserHandler() {
    }

    public static UserHandler getInstance() {
        if (handler == null) {
            synchronized (UserHandler.class) {
                if (handler == null) {
                    handler = new UserHandler();
                }
            }
        }
        return handler;
    }

    @Override
    public void bindHandlerMethodMapper(HandlerMethodMapper handlerMethodMapper) {
        this.handlerMethodMapper = handlerMethodMapper;
    }

    @Override
    public HttpResponse service(HttpRequest request) throws IOException {
        HandlerMethod handlerMethod = handlerMethodMapper.getHandlerMethod(
                new Pair<>(
                        request.getMethod(),
                        getCurrentPath(request.getRequestURI(), HandlerMethodMapper.depth)
                )
        );
        return handlerMethod.service(request);
    }
}
