package model.handler;

import model.http.request.HttpServletRequest;

public interface HandlerMapping {
    Handler getHandler(HttpServletRequest request);
}
