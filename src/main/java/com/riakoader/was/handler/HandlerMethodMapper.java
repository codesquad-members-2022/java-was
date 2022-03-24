package com.riakoader.was.handler;

import com.riakoader.was.db.DataBase;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import com.riakoader.was.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class HandlerMethodMapper {

    private static final Logger logger = LoggerFactory.getLogger(HandlerMethodMapper.class);

    private static final Map<String, HandlerMethod> mapper = new HashMap<>();

    private static final HandlerMethod resourceHandlerMethod = (request) -> {
        HttpResponse response = new HttpResponse();
        response.setStatusLine(request.getProtocol(), HttpStatus.OK);

        FileReader resources= new FileReader("src/main/resources/env.properties");
        Properties properties = new Properties();
        properties.load(resources);

        byte[] body = Files.readAllBytes(new File(properties.getProperty("webapp_path") + request.getRequestURI()).toPath());
        response.setHeader("Content-Length", Integer.toString(body.length));
        response.setBody(body);

        return response;
    };

    static {
        mapper.put("/user/create", (request) -> {
            User user = new User(request.getParameter("userId"), request.getParameter("password"),
                request.getParameter("name"), request.getParameter("email"));

            DataBase.addUser(user);
            logger.debug("user: {}", DataBase.findAll());

            HttpResponse response = new HttpResponse();
            response.setStatusLine(request.getProtocol(), HttpStatus.OK);

            return response;
        });
    }

    public static HandlerMethod getHandlerMethod(String uri) {
        return mapper.getOrDefault(uri, resourceHandlerMethod);
    }
}
