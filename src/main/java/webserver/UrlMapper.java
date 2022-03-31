package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.MainController;
import webserver.controller.UserController;
import webserver.login.SessionDataBase;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UrlMapper {
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private static MainController mainController = new MainController();
    private static UserController userController = new UserController();

    private UrlMapper() {
    }

    public static HttpResponse getResponse(Request request) {
        String url = request.getPath();
        log.debug(url);
        HttpResponse httpResponse = new HttpResponse(request.getVersion());
        Map<String, String> params = request.getParams();
        if (request.isGetMethod()) {
            if (!checkLogin(url, request)) {
                return httpResponse.redirect("/user/login.html");
            }

            switch (url) {
                case "/index.html":
                    return mainController.main(url, httpResponse);
                case "/user/form.html":
                    return userController.joinForm(url, httpResponse);
                case "/user/login.html":
                    return userController.loginForm(url, httpResponse);
                case "/user/logout":
                    return userController.logout(request.getCookie(), httpResponse);
            }
        } else if (request.isPostMethod()) {
            switch (url) {
                case "/user/create":
                    return userController.join(params, httpResponse);
                case "/user/login":
                    return userController.login(params, httpResponse);
            }
        }
        return httpResponse.badRequest();
    }

    private static boolean checkLogin(String url, Request request) {
        List<String> loggedUrls = interceptorLoginUrl();
        boolean isLoggedIn = SessionDataBase.isLoggedIn(request.getCookie());
        return (!loggedUrls.contains(url) || isLoggedIn);
    }

    private static List<String> interceptorLoginUrl() {
        List<String> loggedUrls = new ArrayList<>();
        loggedUrls.add("/user/profile");
        return loggedUrls;
    }
}
