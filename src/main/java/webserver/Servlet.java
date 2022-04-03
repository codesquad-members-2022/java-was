package webserver;

import model.http.request.HttpServletRequest;
import model.http.response.HttpServletResponse;

public interface Servlet {
    void init();

    void service(HttpServletRequest request, HttpServletResponse response) throws Exception;

    void destroy();
}
