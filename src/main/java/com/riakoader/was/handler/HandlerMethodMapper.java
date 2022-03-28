package com.riakoader.was.handler;

import com.riakoader.was.config.HandlerMethodRegistry;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HandlerMethodMapper {

    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodMapper.class);

    private static volatile HandlerMethodMapper handlerMethodMapper;

    private final HandlerMethodRegistry handlerMethodRegistry = HandlerMethodRegistry.getInstance();

    private Map<Pair<String, String>, HandlerMethod> mapper = new HashMap<>();

    private HandlerMethodMapper() {}

    public static HandlerMethodMapper getInstance() {
        if (handlerMethodMapper == null) {
            handlerMethodMapper = new HandlerMethodMapper();
        }
        return handlerMethodMapper;
    }

    private static final HandlerMethod resourceHandlerMethod = (request) -> {
        HttpResponse response = new HttpResponse(request.getProtocol());

        FileReader resources = null;
        try {
            resources = new FileReader("src/main/resources/env.properties");
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        Properties properties = new Properties();
        properties.load(resources);

        byte[] body = Files.readAllBytes(new File(properties.getProperty("webapp_path") + request.getRequestURI()).toPath());

        response.setStatus(HttpStatus.OK);
        response.setHeader("Content-Length", Integer.toString(body.length));
        response.setBody(body);

        return response;
    };

    public HandlerMethod getHandlerMethod(Pair<String, String> pair) {
        return mapper.getOrDefault(pair, handlerMethodRegistry.getHandlerMethod(0));
    }

    public void setHandlerMethodMapper(Map<Pair<String, String>, HandlerMethod> handlerMethodMapper) {
        mapper = handlerMethodMapper;
    }
}
