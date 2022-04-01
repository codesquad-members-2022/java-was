package model.handler;

import model.request.HttpServletRequest;

public class RequestMapping implements HandlerMapping {

    private static final int URL_INDEX = 0;
    private static final String URL_DELIMETER = "\\.";

    private RequestMapping (){};
    private static final RequestMapping instance = new RequestMapping();

    public static RequestMapping getInstance() {
        if (instance == null) {
            return new RequestMapping();
        }
        return instance;
    }

    @Override
    public Handler getHandler(HttpServletRequest request) {
        String requestURL = parseURL(request.getRequestURL());
        return HandlerFactory.getHandler(requestURL);
    }

    private String parseURL(String requestURL) {
        return requestURL.split(URL_DELIMETER)[URL_INDEX];
    }
}
