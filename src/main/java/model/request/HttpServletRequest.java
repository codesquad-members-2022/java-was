package model.request;

public interface HttpServletRequest {
    String getRequestURL();

    boolean isGet();

    boolean isPost();
}
