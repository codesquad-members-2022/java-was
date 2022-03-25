package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class UrlMapper {
    private UrlMapper() {

    }

    private static final UserController userController = new UserController();
    private static final IndexController indexController = new IndexController();

    public static HttpResponse getResponse(HttpRequest request) {
        String url = request.getUrl();
        return getResponse(url, request);
    }

    private static HttpResponse getResponse(String url, HttpRequest request) {
        HttpResponse response = null;
        switch (url) {
            case "/user/create":
                return userController.join(request);
            case "/index.html":
                return indexController.main(request);
            case "/user/form.html":
                return userController.joinForm(request);
            default:
                return response;
        }
    }
}
