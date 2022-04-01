package com.riakoader.was.config;

import com.riakoader.was.context.WebServerContext;
import com.riakoader.was.db.DataBase;
import com.riakoader.was.handler.*;
import com.riakoader.was.handlermethod.HandlerMethodMapper;
import com.riakoader.was.httpmessage.HttpMethod;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.model.User;
import com.riakoader.was.session.Cookie;
import com.riakoader.was.session.HttpSession;
import com.riakoader.was.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class WebServerConfig implements WebServerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebServerConfig.class);

    private static volatile WebServerConfig webServerConfig;

    final WebServerContext webServerContext = new WebServerContext();

    private WebServerConfig() throws Exception {

        logger.debug("WebServerConfig() start");

        addHandler((HandlerRegistry) webServerContext.getBean("handlerRegistry"));
        configureHandlerMapper((HandlerMapper) webServerContext.getBean("handlerMapper"));

        addHandlerMethodMapper((HandlerMethodMapperRegistry) webServerContext.getBean("handlerMethodMapperRegistry"));

        bindMethodsToHandler(
                (HandlerRegistry) webServerContext.getBean("handlerRegistry"),
                (HandlerMethodMapperRegistry) webServerContext.getBean("handlerMethodMapperRegistry")
        );

        logger.debug("WebServerConfig() end");
    }

    public static WebServerConfig getInstance() throws Exception {
        if (webServerConfig == null) {
            synchronized (WebServerConfig.class) {
                if (webServerConfig == null) {
                    webServerConfig = new WebServerConfig();
                }
            }
        }
        return webServerConfig;
    }

    @Override
    public void addHandler(HandlerRegistry handlerRegistry) throws Exception {

        logger.debug("addHandlerMethod() start");

        handlerRegistry.addHandler((UserHandler) webServerContext.getBean("userHandler"));

        logger.debug("addHandlerMethod() end");
    }

    @Override
    public void configureHandlerMapper(HandlerMapper handlerMapper) {
        logger.debug("configureHandlerMethod() start");

        handlerMapper.mappingHandler("/user", 0);

        logger.debug("configureHandlerMethod() start");
    }

    @Override
    public void addHandlerMethodMapper(HandlerMethodMapperRegistry handlerMethodMapperRegistry) {
        logger.debug("addHandlerMethodMapper() start");

        handlerMethodMapperRegistry.addHandlerMethod(
                new HandlerMethodMapper(
                        Map.of(
                                new Pair<>(HttpMethod.GET.name(), "/create"),
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
                                },

                                new Pair<>(HttpMethod.POST.name(), "/create"),
                                (request) -> {

                                    if (DataBase.findUserById(request.getParameter("userId")) != null) {
                                        HttpResponse response = new HttpResponse(request.getProtocol());
                                        response.setHeader("Location", "/user/form.html");
                                        response.setStatus(HttpStatus.FOUND);

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
                                    response.setHeader("Location", "/index.html");
                                    response.setStatus(HttpStatus.FOUND);

                                    return response;
                                },

                                new Pair<>(HttpMethod.POST.name(), "/login"),
                                (request) -> {

                                    logger.debug("cookies: {}", request.getCookies());

                                    User user = DataBase.findUserById(request.getParameter("userId"));

                                    logger.debug("user: {}", user);
                                    logger.debug("request password: {}", request.getParameter("password"));

                                    HttpResponse response = new HttpResponse(request.getProtocol());
                                    if (!user.getPassword().equals(request.getParameter("password"))) {
                                        response.setHeader("Location", "/user/login_failed.html");
                                        response.setStatus(HttpStatus.FOUND);

                                        return response;
                                    }

                                    HttpSession session = request.getSession();
                                    session.setAttribute("sessionId", UUID.randomUUID().toString());

                                    Cookie cookie = new Cookie("sessionId", session.getAttribute("sessionId"));
                                    cookie.setPath("/");
                                    response.addCookie(cookie);

                                    response.setHeader("Location", "/index.html");
                                    response.setStatus(HttpStatus.FOUND);

                                    return response;
                                },

                                new Pair<>(HttpMethod.GET.name(), "/logout"),
                                (request) -> {

                                    logger.debug("cookies: {}", request.getCookies());

                                    HttpResponse response = new HttpResponse(request.getProtocol());

                                    HttpSession session = request.getSession();

                                    Cookie cookie = new Cookie("sessionId", session.getAttribute("sessionId"));
                                    cookie.setMaxAge(0);
                                    cookie.setPath("/");

                                    response.addCookie(cookie);
                                    response.setHeader("Location", "/index.html");
                                    response.setStatus(HttpStatus.FOUND);

                                    session.removeAttribute("sessionId");

                                    logger.debug("cookies: {}", request.getCookies());

                                    return response;
                                }
                        )
                )
        );

        logger.debug("addHandlerMethodMapper() end");
    }

    @Override
    public void bindMethodsToHandler(HandlerRegistry handlerRegistry,
                                     HandlerMethodMapperRegistry handlerMethodMapperRegistry) {

        int handlerCount = handlerRegistry.size();
        for (int index = 0; index < handlerCount; ++index) {
            Handler handler = handlerRegistry.getHandler(index);
            HandlerMethodMapper handlerMethodMapper = handlerMethodMapperRegistry.getHandlerMethod(index);
            handler.bindHandlerMethodMapper(handlerMethodMapper);
        }
    }
}
