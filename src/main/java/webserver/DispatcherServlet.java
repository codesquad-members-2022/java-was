package webserver;

import configuration.ObjectFactory;
import model.handler.Handler;
import model.handler.HandlerMapping;
import model.request.HttpServletRequest;
import model.response.HttpServletResponse;

import java.io.IOException;

public class DispatcherServlet implements Servlet {

    private HandlerMapping handlerMapping;
    private static final DispatcherServlet instance = new DispatcherServlet();

    private DispatcherServlet() {
        init();
    }

    public static DispatcherServlet getInstance() {
        if (instance == null) {
            return new DispatcherServlet();
        }
        return instance;
    }

    @Override
    public void init() {
        this.handlerMapping = ObjectFactory.requestMapping;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Handler handler = handlerMapping.getHandler(request);
        handler.service(request, response);
    }

    @Override
    public void destroy() {

    }
}
