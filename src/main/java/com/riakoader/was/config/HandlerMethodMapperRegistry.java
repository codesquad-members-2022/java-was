package com.riakoader.was.config;

import com.riakoader.was.handlermethod.HandlerMethodMapper;

import java.util.ArrayList;
import java.util.List;

public class HandlerMethodMapperRegistry {

    private static volatile HandlerMethodMapperRegistry handlerMethodRegistry;

    private final List<HandlerMethodMapper> registry = new ArrayList<>();

    private HandlerMethodMapperRegistry() {
    }

    public static HandlerMethodMapperRegistry getInstance() {
        if (handlerMethodRegistry == null) {
            synchronized (HandlerMethodMapperRegistry.class) {
                if (handlerMethodRegistry == null) {
                    handlerMethodRegistry = new HandlerMethodMapperRegistry();
                }
            }
        }
        return handlerMethodRegistry;
    }

    public void addHandlerMethod(HandlerMethodMapper handlerMethodMapper) {
        registry.add(handlerMethodMapper);
    }

    public HandlerMethodMapper getHandlerMethod(int index) {
        return registry.get(index);
    }
}
