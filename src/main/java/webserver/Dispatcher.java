package webserver;

import config.RequestMapping;
import http.HttpServlet;
import http.Request;
import http.Response;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

public class Dispatcher {

    private final RequestMapping requestMapping;

    public Dispatcher(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public boolean isMappedUrl(String url) {
        return requestMapping.contains(url);
    }

    public Response handleRequest(HttpRequestData requestData) {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();
        HttpServlet httpServlet = requestMapping.findHandlerMethod(httpRequestLine.getUrl())
            .orElseThrow(() -> new IllegalStateException("Mapped servlet could not be found"));

        Request request = Request.of(requestData);
        Response response = new Response();

        return httpServlet.service(request, response);
    }
}

