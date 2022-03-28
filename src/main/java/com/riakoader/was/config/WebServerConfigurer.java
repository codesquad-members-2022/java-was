package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMethod;
import com.riakoader.was.util.Pair;

import java.util.Map;

public interface WebServerConfigurer {

    default void addHandlerMethod(HandlerMethodRegistry handlerMethodRegistry) {}

    default void configureHandlerMethodMapper(Map<Pair<String, String>, HandlerMethod> handlerMethods) {}
}
