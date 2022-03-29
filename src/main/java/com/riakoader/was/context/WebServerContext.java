package com.riakoader.was.context;

import com.riakoader.was.config.HandlerMethodRegistry;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.webserver.FrontHandler;

public class WebServerContext extends BeanFactory {

    final FrontHandler frontHandler;

    final HandlerMethodRegistry handlerMethodRegistry;

    final HandlerMethodMapper handlerMethodMapper;

    public WebServerContext() {
        super();
        frontHandler = FrontHandler.getInstance();
        handlerMethodRegistry = HandlerMethodRegistry.getInstance();
        handlerMethodMapper = HandlerMethodMapper.getInstance();
    }
}
