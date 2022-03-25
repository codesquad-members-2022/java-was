package webserver.controller;

import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

public class UrlMapper {
    private UrlMapper() {
        
    }

    private static final JoinController joinController = new JoinController();
    private static final IndexController indexController = new IndexController();

    public static HttpResponse getResponse(HttpRequest request) {
        String url = request.getUrl();
        return getResponse(url, request);
    }

    private static HttpResponse getResponse(String url, HttpRequest request) {
        HttpResponse response = null;
        switch (url) {
            case "/user/create":
                return joinController.join(request);
            case "/index.html":
                return indexController.main(request);
            case "/user/form.html":
                return indexController.userForm(request);
            default:
                return response;
        }
    }
}
