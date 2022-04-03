package servlet.filter;

import http.HttpRequest;
import http.HttpResponse;

public interface Filter {
    public boolean doFilter(HttpRequest request, HttpResponse response);
}
