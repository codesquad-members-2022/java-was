package com.riakoader.was.handler;

import com.riakoader.was.handlermethod.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;

import java.io.IOException;

public interface Handler {

    int depth = 1;

    void bindHandlerMethodMapper(HandlerMethodMapper handlerMethodMapper);

    HttpResponse service(HttpRequest request) throws IOException;
}
