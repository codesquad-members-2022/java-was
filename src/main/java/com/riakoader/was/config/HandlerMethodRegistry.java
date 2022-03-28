package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMethod;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HandlerMethodRegistry {

    private static volatile HandlerMethodRegistry handlerMethodRegistry;

    private final List<HandlerMethod> registry = new CopyOnWriteArrayList<>();

    public static HandlerMethodRegistry getInstance() {
        if (handlerMethodRegistry == null) {
            handlerMethodRegistry = new HandlerMethodRegistry();
        }
        return handlerMethodRegistry;
    }

    private HandlerMethodRegistry() {}

    public void setHandlerMethod(HandlerMethod handlerMethod) {
        registry.add(handlerMethod);
    }

    public HandlerMethod getHandlerMethod(int index) {
        return registry.get(index);
    }
}
