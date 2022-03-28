package com.riakoader.was.config;

import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpMethod;
import com.riakoader.was.util.Pair;
import com.riakoader.was.webserver.WebServerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

public class WebServerConfig implements WebServerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebServerConfig.class);

    private static volatile WebServerConfig webServerConfig;

    private final WebServerContext webServerContext;

    private WebServerConfig() throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, InstantiationException, IllegalAccessException {

        logger.debug("WebServerConfig() start");

        webServerContext = WebServerContext.getInstance();

        addHandlerMethod((HandlerMethodRegistry) webServerContext.getBean("handlerMethodRegistry"));
        configureHandlerMethodMapper((HandlerMethodMapper) webServerContext.getBean("handlerMethodMapper"));

        logger.debug("WebServerConfig() end");
    }

    public static WebServerConfig getInstance() throws NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {

        if (webServerConfig == null) {
            webServerConfig = new WebServerConfig();
        }
        return webServerConfig;
    }

    public void addHandlerMethod(HandlerMethodRegistry handlerMethodRegistry) {
        logger.debug("addHandlerMethod() start");

        handlerMethodRegistry.addHandlerMethod(webServerContext.resourceHandlerMethod);
        handlerMethodRegistry.addHandlerMethod(webServerContext.joinHandlerMethodForGet);
        handlerMethodRegistry.addHandlerMethod(webServerContext.joinHandlerMethodForPost);

        logger.debug("addHandlerMethod() end");
    }

    public void configureHandlerMethodMapper(HandlerMethodMapper handlerMethodMapper) throws NoSuchFieldException,
            ClassNotFoundException, InvocationTargetException, NoSuchMethodException,
            IllegalAccessException {

        logger.debug("configureHandlerMethodMapper() start");

        HandlerMethodRegistry handlerMethodRegistry = (HandlerMethodRegistry) webServerContext.getBean("handlerMethodRegistry");

        handlerMethodMapper.mappingHandlerMethod(
                new Pair<>(HttpMethod.GET.name(), "/user/create"),
                handlerMethodRegistry.getHandlerMethod(1)
        );

        handlerMethodMapper.mappingHandlerMethod(
                new Pair<>(HttpMethod.POST.name(), "/user/create"),
                handlerMethodRegistry.getHandlerMethod(2)
        );

        logger.debug("configureHandlerMethodMapper() end");
    }
}
