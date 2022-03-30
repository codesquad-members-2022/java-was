package webserver;

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

    public Response handleRequest(HttpRequestData requestData) {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();
        HttpServlet httpServlet = RequestMapping.findHandlerMethod(httpRequestLine.getUrl())
            .orElseThrow(() -> new IllegalStateException("Mapped servlet could not be found"));

        Request request = Request.of(requestData);
        Response response = new Response();

        return httpServlet.service(request, response);
    }
}

