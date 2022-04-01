package com.riakoader.was.context;

import com.riakoader.was.config.HandlerMethodMapperRegistry;
import com.riakoader.was.config.HandlerRegistry;
import com.riakoader.was.handler.*;
import com.riakoader.was.webserver.FrontHandler;

public class WebServerContext extends BeanFactory {

    final FrontHandler frontHandler;

    /**
     * Registry
     */
    final HandlerRegistry handlerRegistry;
    final HandlerMethodMapperRegistry handlerMethodMapperRegistry;

    /**
     * Mapper
     */
    final HandlerMapper handlerMapper;

    /**
     * Handler
     */
    final ResourceHandler resourceHandler;
    final UserHandler userHandler;

    public WebServerContext() {
        super();

        frontHandler = FrontHandler.getInstance();

        handlerRegistry = HandlerRegistry.getInstance();
        handlerMethodMapperRegistry = HandlerMethodMapperRegistry.getInstance();

        handlerMapper = HandlerMapper.getInstance();

        resourceHandler = ResourceHandler.getInstance();
        userHandler = UserHandler.getInstance();
    }
}
