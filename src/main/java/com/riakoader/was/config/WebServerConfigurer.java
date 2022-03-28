package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMethodMapper;

public interface WebServerConfigurer {

    default void addHandlerMethod(HandlerMethodRegistry handlerMethodRegistry) {
    }

    default void configureHandlerMethodMapper(HandlerMethodMapper handlerMethodMapper) {
    }
}
