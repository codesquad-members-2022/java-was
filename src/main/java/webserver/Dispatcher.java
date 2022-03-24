package webserver;

import java.lang.reflect.Constructor;

import config.RequestMapping;
import http.HttpServlet;
import http.Request;
import http.Response;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

public class Dispatcher {

    private static Dispatcher instance;

    private Dispatcher() {
    }

    public synchronized static Dispatcher getInstance() {
        if (instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    public Response handleRequest(HttpRequestData requestData) throws Exception {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();
        Class<? extends HttpServlet> servletClass = RequestMapping.findHandlerMethod(httpRequestLine.getUrl())
            .orElseThrow(() -> new IllegalStateException("Mapped servlet could not be found"));

        Request request = Request.of(requestData);
        Response response = new Response();

        Constructor<? extends HttpServlet> constructor = servletClass.getConstructor(Request.class, Response.class);
        HttpServlet httpServlet = constructor.newInstance(request, response);

        return httpServlet.service();
    }
}

