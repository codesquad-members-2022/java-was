package webserver.controller;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.http.Request;
import webserver.http.Response;

public class UserJoinController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserJoinController.class);

    @Override
    public void process(Request request, Response response) throws IOException {
        try {
            Map<String, String> userData = HttpRequestUtils.parseQueryString(request.getRequestBody().orElseThrow());
            User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"),
                userData.get("email"));
            DataBase.addUser(user).ifPresentOrElse(
                value -> response.response302Header("/index.html"),
                () -> response.response302Header("/user/form.html")
            );

        } catch (NoSuchElementException e) {
            log.error(e.getMessage());
        }
    }
}
