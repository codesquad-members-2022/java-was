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

public class UserLoginController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Override
    public void process(Request request, Response response) throws IOException {

        try {
            Map<String, String> userData = HttpRequestUtils.parseQueryString(request.getRequestBody().orElseThrow());
            //TODO : userData가 null일 경우 USER_NOT_FOUND_EXCEPTION 처리

            User loginUser = DataBase.findUserById(userData.get("userId")).orElseThrow();
            if (loginUser.matchesPassword(userData.get("password"))) {
                response.response302HeaderWithCookie("/index.html", "sessionId=" + loginUser.getUserId());
                return;
            }
            response.response302Header("/user/login_failed.html");
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }
    }
}
