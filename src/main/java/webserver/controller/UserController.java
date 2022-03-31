package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.Request;
import webserver.login.Cookie;
import webserver.login.SessionDataBase;

import java.util.Map;
import java.util.Optional;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String INDEX_PAGE_URL = "/index.html";
    private static final String JOIN_PAGE_URL = "/user/form.html";
    private static final String LOGIN_FAILED_URL = "/user/login_failed.html";

    public HttpResponse joinForm(Request request, HttpResponse httpResponse) {
        return httpResponse.ok(request.getPath());
    }

    public HttpResponse join(Request request, HttpResponse httpResponse) {
        Map<String, String> body = request.getParams();
        User user = new User(body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email"));
        try {
            DataBase.addUser(user);
        } catch (IllegalArgumentException exception) {
            log.error(exception.getMessage());
            return httpResponse.ok(JOIN_PAGE_URL);
        }
        return httpResponse.redirect(INDEX_PAGE_URL);
    }

    public HttpResponse loginForm(Request request, HttpResponse httpResponse) {
        return httpResponse.ok(request.getPath());
    }

    public HttpResponse login(Request request, HttpResponse httpResponse) {
        Map<String, String> body = request.getParams();
        String loginUserId = body.get("userId");
        String loginPassword = body.get("password");

        Optional<User> user = DataBase.findUserById(loginUserId);
        if (user.isPresent() && user.get().isMatchPassword(loginPassword)) {
            Cookie cookie = new Cookie(loginUserId);
            SessionDataBase.addCookie(cookie);
            return httpResponse.login(INDEX_PAGE_URL, cookie);
        }
        return httpResponse.redirect(LOGIN_FAILED_URL);
    }

    public HttpResponse logout(Request request, HttpResponse httpResponse) {
        SessionDataBase.deleteCookie(request.getCookie());
        return httpResponse.redirect(INDEX_PAGE_URL);
    }
}
