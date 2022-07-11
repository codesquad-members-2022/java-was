package com.riakoader.was.handlermethod;

import com.riakoader.was.util.Pair;

import java.util.Map;

public class HandlerMethodMapper {

    public static final int DEPTH = 2;

    private final Map<Pair<String, String>, HandlerMethod> mapper;

    public HandlerMethodMapper(Map<Pair<String, String>, HandlerMethod> mapper) {
        this.mapper = mapper;
    }

    public HandlerMethod getHandlerMethod(Pair<String, String> pair) {
        return mapper.get(pair);
    }
}
