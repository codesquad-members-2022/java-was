package com.riakoader.was.handler;

import com.riakoader.was.config.HandlerRegistry;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapper {

    private static volatile HandlerMapper handlerMapper;

    private final HandlerRegistry handlerRegistry = HandlerRegistry.getInstance();

    private final Map<String, Handler> mapper = new HashMap<>();

    private HandlerMapper() {
    }

    public static HandlerMapper getInstance() {
        if (handlerMapper == null) {
            handlerMapper = new HandlerMapper();
        }
        return handlerMapper;
    }

    public Handler getHandler(String url) {
        return mapper.get(url);
    }

    public void mappingHandler(String url, int handlerIndex) {
        mapper.put(url, handlerRegistry.getHandler(handlerIndex));
    }
}
