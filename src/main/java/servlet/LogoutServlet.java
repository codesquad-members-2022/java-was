package servlet;

import http.HttpRequest;
import http.HttpResponse;

public class LogoutServlet extends BaseServlet {
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        response.addHeader("Cache-Control", "max-age=0");
    }
}
