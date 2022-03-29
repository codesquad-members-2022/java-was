package com.riakoader.was.config;

import com.riakoader.was.context.WebServerContext;
import com.riakoader.was.db.DataBase;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpMethod;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.model.User;
import com.riakoader.was.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Properties;

public class WebServerConfig implements WebServerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebServerConfig.class);

    private static volatile WebServerConfig webServerConfig;

    final WebServerContext webServerContext = new WebServerContext();

    private WebServerConfig() throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

        logger.debug("WebServerConfig() start");

        addHandlerMethod((HandlerMethodRegistry) webServerContext.getBean("handlerMethodRegistry"));
        configureHandlerMethodMapper((HandlerMethodMapper) webServerContext.getBean("handlerMethodMapper"));

        logger.debug("WebServerConfig() end");
    }

    public static WebServerConfig getInstance() throws NoSuchFieldException, ClassNotFoundException,
            InvocationTargetException, NoSuchMethodException, IllegalAccessException {

        if (webServerConfig == null) {
            webServerConfig = new WebServerConfig();
        }
        return webServerConfig;
    }

    @Override
    public void addHandlerMethod(HandlerMethodRegistry handlerMethodRegistry) {
        logger.debug("addHandlerMethod() start");

        handlerMethodRegistry.addHandlerMethod(
                (request) -> {

                    HttpResponse response = new HttpResponse(request.getProtocol());

                    FileReader resources;
                    try {
                        resources = new FileReader("src/main/resources/env.properties");
                    } catch (FileNotFoundException e) {
                        logger.error(e.getMessage());
                        response.setStatus(HttpStatus.NOT_FOUND);
                        return response;
                    }

                    Properties properties = new Properties();
                    properties.load(resources);

                    byte[] body = Files.readAllBytes(
                            new File(properties.getProperty("webapp_path") + request.getRequestURI()).toPath()
                    );

                    response.setStatus(HttpStatus.OK);
                    response.setHeader("Content-Length", Integer.toString(body.length));
                    response.setBody(body);

                    return response;
                });

        handlerMethodRegistry.addHandlerMethod(
                (request) -> {

                    User user = new User(
                            request.getParameter("userId"),
                            request.getParameter("password"),
                            request.getParameter("name"),
                            request.getParameter("email")
                    );

                    DataBase.addUser(user);
                    logger.debug("user: {}", DataBase.findAll());

                    HttpResponse response = new HttpResponse(request.getProtocol());
                    response.setStatus(HttpStatus.CREATED);

                    return response;
                });

        handlerMethodRegistry.addHandlerMethod(
                (request) -> {

                    if (DataBase.findUserById(request.getParameter("userId")) != null) {
                        HttpResponse response = new HttpResponse(request.getProtocol());
                        response.setStatus(HttpStatus.FOUND);
                        response.setHeader("Location", "/user/form.html");

                        return response;
                    }

                    User user = new User(
                            request.getParameter("userId"),
                            request.getParameter("password"),
                            request.getParameter("name"),
                            request.getParameter("email")
                    );

                    DataBase.addUser(user);
                    logger.debug("user: {}", DataBase.findAll());

                    HttpResponse response = new HttpResponse(request.getProtocol());
                    response.setStatus(HttpStatus.FOUND);
                    response.setHeader("Location", "/index.html");

                    return response;
                });

        logger.debug("addHandlerMethod() end");
    }

    @Override
    public void configureHandlerMethodMapper(HandlerMethodMapper handlerMethodMapper) {
        logger.debug("configureHandlerMethodMapper() start");

        handlerMethodMapper.mappingHandlerMethod(new Pair<>(HttpMethod.GET.name(), "/user/create"), 1);
        handlerMethodMapper.mappingHandlerMethod(new Pair<>(HttpMethod.POST.name(), "/user/create"), 2);

        logger.debug("configureHandlerMethodMapper() end");
    }
}
