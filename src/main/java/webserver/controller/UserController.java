package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String INDEX_PAGE_URL = "/index.html";

    public HttpResponse joinForm(String url) {
        HttpResponse response = response200Header(request);
        try {
            response.addBody(Files.readAllBytes(new File("./webapp" + url).toPath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public HttpResponse join(Map<String, String> body) {
        HttpResponse response;
        User user = new User(body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email"));
        DataBase.addUser(user);
        response = new HttpResponse(request.getVersion(), HttpStatus.FOUND);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Location", INDEX_PAGE_URL);
        log.debug("Status Code : {}", response.getHttpStatusCode());
        log.debug("Location : {}", response.getHeader("Location"));
        return response;

    }
}
