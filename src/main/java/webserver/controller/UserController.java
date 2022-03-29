package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.IOException;
import java.util.Map;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String INDEX_PAGE_URL = "/index.html";

    public HttpResponse joinForm(String url, HttpResponse httpResponse) {
        return httpResponse.ok(url);
    }

    public HttpResponse join(Map<String, String> body, HttpResponse httpResponse) {
        User user = new User(body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email"));
        try {
            DataBase.addUser(user);
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return httpResponse.ok("/user/form.html");
        }
        return httpResponse.redirect(INDEX_PAGE_URL);
    }
}
