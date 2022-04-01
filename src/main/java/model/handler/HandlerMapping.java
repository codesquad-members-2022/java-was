package model.handler;

import model.request.HttpServletRequest;

public interface HandlerMapping {
    Handler getHandler(HttpServletRequest request);
}
