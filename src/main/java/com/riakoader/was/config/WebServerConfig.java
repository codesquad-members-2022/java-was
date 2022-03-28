package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMethod;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.util.Pair;
import com.riakoader.was.webserver.WebServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WebServerConfig implements WebServerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebServerConfig.class);

    private static volatile WebServerConfig webServerConfig;

    private final WebServerContext webServerContext = WebServerContext.getInstance();

    public static WebServerConfig getInstance() {
        if (webServerConfig == null) {
            webServerConfig = new WebServerConfig();
        }
        return webServerConfig;
    }

    private WebServerConfig() {
        logger.debug("WebServerConfig() start");

        HandlerMethodRegistry handlerMethodRegistry = HandlerMethodRegistry.getInstance();
        addHandlerMethod(handlerMethodRegistry);
        configureHandlerMethodMapper(
                Map.of(
                        new Pair<>("GET", "/user/create"), handlerMethodRegistry.getHandlerMethod(1),
                        new Pair<>("POST", "/user/create"), handlerMethodRegistry.getHandlerMethod(2)
                ));

        logger.debug("WebServerConfig() end");
    }

    public void addHandlerMethod(HandlerMethodRegistry handlerMethodRegistry) {
        logger.debug("addHandlerMethod() start");

        handlerMethodRegistry.setHandlerMethod(webServerContext.resourceHandlerMethod);
        handlerMethodRegistry.setHandlerMethod(webServerContext.joinHandlerMethodForGet);
        handlerMethodRegistry.setHandlerMethod(webServerContext.joinHandlerMethodForPost);

        logger.debug("addHandlerMethod() end");
    }

    public void configureHandlerMethodMapper(Map<Pair<String, String>, HandlerMethod> handlerMethods) {
        logger.debug("configureHandlerMethodMapper() start");

        HandlerMethodMapper handlerMethodMapper = HandlerMethodMapper.getInstance();
        handlerMethodMapper.setHandlerMethodMapper(handlerMethods);

        logger.debug("configureHandlerMethodMapper() end");
    }
}
