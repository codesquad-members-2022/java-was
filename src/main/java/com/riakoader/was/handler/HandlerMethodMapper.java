package com.riakoader.was.handler;

import com.riakoader.was.config.HandlerMethodRegistry;
import com.riakoader.was.util.Pair;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerMethodMapper {

    private static volatile HandlerMethodMapper handlerMethodMapper;

    private final HandlerMethodRegistry handlerMethodRegistry = HandlerMethodRegistry.getInstance();

    private final Map<Pair<String, String>, HandlerMethod> mapper = new ConcurrentHashMap<>();

    private HandlerMethodMapper() {}

    public static HandlerMethodMapper getInstance() {
        if (handlerMethodMapper == null) {
            handlerMethodMapper = new HandlerMethodMapper();
        }
        return handlerMethodMapper;
    }

    public HandlerMethod getHandlerMethod(Pair<String, String> pair) {
        return mapper.getOrDefault(pair, handlerMethodRegistry.getHandlerMethod(0));
    }

    public void mappingHandlerMethod(Pair<String, String> pair, int handlerMethodIndex) {
        mapper.put(pair, handlerMethodRegistry.getHandlerMethod(handlerMethodIndex));
    }
}
