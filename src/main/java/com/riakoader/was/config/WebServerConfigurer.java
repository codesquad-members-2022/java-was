package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMapper;

public interface WebServerConfigurer {

    default void addHandler(HandlerRegistry handlerRegistry) throws Exception {
    }

    default void configureHandlerMapper(HandlerMapper handlerMapper) {
    }

    default void addHandlerMethodMapper(HandlerMethodMapperRegistry handlerMethodMapperRegistry) {
    }

    default void bindMethodsToHandler(HandlerRegistry handlerRegistry, HandlerMethodMapperRegistry handlerMethodMapperRegistry) {
    }
}
