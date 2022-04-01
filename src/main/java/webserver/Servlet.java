package webserver;

import model.request.HttpServletRequest;
import model.response.HttpServletResponse;

public interface Servlet {
    void init();

    void service(HttpServletRequest request, HttpServletResponse response) throws Exception;

    void destroy();
}
