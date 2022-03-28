package com.riakoader.was.webserver;

import com.riakoader.was.config.HandlerMethodRegistry;
import com.riakoader.was.db.DataBase;
import com.riakoader.was.handler.HandlerMethod;
import com.riakoader.was.handler.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.Properties;

public class WebServerContext {

    private static final Logger logger = LoggerFactory.getLogger(WebServerContext.class);

    private static volatile WebServerContext webServerContext;

    private final HandlerMethodRegistry handlerMethodRegistry;

    private final HandlerMethodMapper handlerMethodMapper;

    private WebServerContext() {
        handlerMethodRegistry = HandlerMethodRegistry.getInstance();
        handlerMethodMapper = HandlerMethodMapper.getInstance();
    }

    public static WebServerContext getInstance() {
        if (webServerContext == null) {
            webServerContext = new WebServerContext();
        }
        return webServerContext;
    }

    public Object getBean(String beanName) throws NoSuchFieldException, ClassNotFoundException, NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {

        Class<?> context = Class.forName(this.getClass().getTypeName());

        Field field = context.getDeclaredField(beanName);
        field.setAccessible(true);

        Class<?> clazz = Class.forName(field.getType().getTypeName());
        Method method = clazz.getDeclaredMethod("getInstance");

        return method.invoke(null);
    }

    public HandlerMethod resourceHandlerMethod =
            (request) -> {

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
            };

    public HandlerMethod joinHandlerMethodForGet =
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
            };

    public HandlerMethod joinHandlerMethodForPost =
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
            };
}
