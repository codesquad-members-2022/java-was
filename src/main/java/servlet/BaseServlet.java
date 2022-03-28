package servlet;

import http.HttpRequest;
import http.HttpResponse;

public class BaseServlet implements Servlet {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        String method = request.getMethod();
        if (method.equals("GET")) {
            doGet(request, response);
        } else {
            doPost(request, response);
        }
    }

    public void doGet(HttpRequest request, HttpResponse response) {
    }

    ;

    public void doPost(HttpRequest request, HttpResponse response) {
    }

    ;
}
