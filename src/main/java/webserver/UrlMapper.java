package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.IndexController;
import webserver.controller.UserController;

import java.io.BufferedReader;
import java.util.Map;

public class UrlMapper {
    private static final Logger log = LoggerFactory.getLogger(UrlMapper.class);
    private static IndexController indexController = new IndexController();
    private static UserController userController = new UserController();

    private UrlMapper() {

    }

    public static HttpResponse getResponse(HttpRequest httpRequest, BufferedReader bufferedReader) {
        HttpResponse response = null;
        String url = httpRequest.getPath();

        if (httpRequest.getMapping()) {
            Map<String, String> queryString = httpRequest.getQueryString();
            switch (url) {
                case "/index.html":
                    return indexController.main(url);
                case "/user/form.html":
                    return userController.joinForm(url);
            }
        } else if (httpRequest.postMapping()) {
            Map<String, String> body = httpRequest.getBody(bufferedReader);
            switch (url) {
                case "/user/create/":
                    return userController.join(body);
            }
        }
        return response;
    }
}
