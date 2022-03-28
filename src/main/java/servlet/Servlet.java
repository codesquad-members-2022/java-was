package servlet;

import http.HttpRequest;
import http.HttpResponse;

public interface Servlet {
    public void service(HttpRequest request, HttpResponse response);
}
