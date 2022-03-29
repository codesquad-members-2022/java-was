package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.MainController;
import webserver.controller.UserController;

import java.io.BufferedReader;
import java.util.Map;

public class UrlMapper {
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private static MainController mainController = new MainController();
    private static UserController userController = new UserController();

    private UrlMapper() {
    }

    public static HttpResponse getResponse(HttpRequest httpRequest, BufferedReader bufferedReader) {
        String url = httpRequest.getPath();
        log.debug(url);
        HttpResponse httpResponse = new HttpResponse(httpRequest.httpVersion());
        if (httpRequest.getMapping()) {
            Map<String, String> queryString = httpRequest.getQueryString();
            switch (url) {
                case "/index.html":
                    return mainController.main(url, httpResponse);
                case "/user/form.html":
                    return userController.joinForm(url, httpResponse);
                case "/user/login.html":
                    return userController.loginForm(url, httpResponse);
            }
        } else if (httpRequest.postMapping()) {
            Map<String, String> body = httpRequest.getBody(bufferedReader);
            switch (url) {
                case "/user/create":
                    return userController.join(body, httpResponse);
                case "/user/login":
                    return userController.login(body, httpResponse);
            }
        }
        return httpResponse.badRequest();
    }
}
