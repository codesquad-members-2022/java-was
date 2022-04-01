package com.riakoader.was.handlermethod;

import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;

import java.io.IOException;

@FunctionalInterface
public interface HandlerMethod {

    HttpResponse service(HttpRequest request) throws IOException;
}
