package com.riakoader.was.config;

import com.riakoader.was.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerRegistry {

    private static volatile HandlerRegistry handlerRegistry;

    private final List<Handler> registry = new ArrayList<>();

    private HandlerRegistry() {
    }

    public static HandlerRegistry getInstance() {
        if (handlerRegistry == null) {
            synchronized (HandlerRegistry.class) {
                if (handlerRegistry == null) {
                    handlerRegistry = new HandlerRegistry();
                }
            }
        }
        return handlerRegistry;
    }

    public void addHandler(Handler handler) {
        registry.add(handler);
    }

    public Handler getHandler(int index) {
        return registry.get(index);
    }

    public int size() {
        return registry.size();
    }
}
