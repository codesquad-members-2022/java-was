package webserver.controller;

import db.SessionDataBase;
import db.UserDataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Cookie;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static webserver.http.HttpResponse.response200Header;

public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String INDEX_PAGE_URL = "/index.html";
    private static final String LOGIN_PAGE_URL = "/user/login.html";
    private static final String JOIN_PAGE_URL = "/user/form.html";

    public HttpResponse joinForm(HttpRequest request) {
        HttpResponse response = response200Header(request);
        try {
            response.addBody(Files.readAllBytes(new File("./webapp" + JOIN_PAGE_URL).toPath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public HttpResponse loginForm(HttpRequest request) {
        HttpResponse response = response200Header(request);
        try {
            response.addBody(Files.readAllBytes(new File("./webapp" + LOGIN_PAGE_URL).toPath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }

    public HttpResponse login(HttpRequest request) {
        HttpResponse response = null;
        String userId = request.getParameter("userId");
        String password = request.getParameter("password");
        User user = UserDataBase.findUserById(userId);

        if (user.isMatchPassword(password)) {
            response = new HttpResponse(request.getVersion(), HttpStatus.FOUND);
            response.addHeader("Content-Type", "text/html;charset=utf-8");
            response.addHeader("Location", INDEX_PAGE_URL);

            Cookie cookie = new Cookie(user);
            String sessionId = SessionDataBase.addCookie(cookie);
            response.addHeader("Set-Cookie", cookie.getCookieString(sessionId));
            log.debug("cookieString: {}", cookie.getCookieString(sessionId));
            log.debug("Status Code : {}", response.getHttpStatusCode());
            log.debug("Location : {}", response.getHeader("Location"));
        }
        return response;
    }

    public HttpResponse join(HttpRequest request) {
        HttpResponse response;
        if (request.getMethod().equals("POST")) {
            User user = new User(request.getParameter("userId"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("email"));
            UserDataBase.addUser(user);
            response = new HttpResponse(request.getVersion(), HttpStatus.FOUND);
            response.addHeader("Content-Type", "text/html;charset=utf-8");
            response.addHeader("Location", INDEX_PAGE_URL);
            log.debug("Status Code : {}", response.getHttpStatusCode());
            log.debug("Location : {}", response.getHeader("Location"));
            return response;
        }
        response = new HttpResponse(request.getVersion(), HttpStatus.METHOD_NOT_ALLOWED);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        return response;
    }


    public HttpResponse logout(HttpRequest request) {
        HttpResponse response = new HttpResponse(request.getVersion(), HttpStatus.FOUND);
        response.addHeader("Content-Type", "text/html;charset=utf-8");
        response.addHeader("Location", INDEX_PAGE_URL);
        String sessionId = request.getSessionId();

        Cookie cookie = SessionDataBase.findByUUID(sessionId);
        cookie.expire();
        log.debug("cookieString : {}", cookie.getCookieString(sessionId));
        response.addHeader("Set-Cookie", cookie.getCookieString(sessionId));

        SessionDataBase.expire(sessionId);

        log.debug("logout, sessionId: {}", sessionId);
        return response;
    }

}
