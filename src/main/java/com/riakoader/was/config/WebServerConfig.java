package com.riakoader.was.config;

import com.riakoader.was.db.DataBase;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpMethod;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.model.User;
import com.riakoader.was.util.Pair;
import com.riakoader.was.webserver.WebServerContext;
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

    private final WebServerContext webServerContext;

    private WebServerConfig() throws NoSuchFieldException, ClassNotFoundException, InvocationTargetException,
            NoSuchMethodException, IllegalAccessException {

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

        handlerMethodRegistry.addHandlerMethod((request) -> {

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

            byte[] body = Files.readAllBytes(
                    new File(properties.getProperty("webapp_path") + request.getRequestURI()).toPath()
            );

            response.setStatus(HttpStatus.OK);
            response.setHeader("Content-Length", Integer.toString(body.length));
            response.setBody(body);

            return response;
        });

        handlerMethodRegistry.addHandlerMethod((request) -> {

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

        handlerMethodRegistry.addHandlerMethod((request) -> {

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
