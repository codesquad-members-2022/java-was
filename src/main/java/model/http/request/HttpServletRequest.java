package model.http.request;

public interface HttpServletRequest {
    String getRequestURL();

    boolean isGet();

    boolean isPost();
}
