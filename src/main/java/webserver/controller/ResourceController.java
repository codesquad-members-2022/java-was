package webserver.controller;

import webserver.ContentType;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceController implements Controller {
    private static final String STATUS200 = "200 OK ";

    @Override
    public Response handleRequest(Request request) {
        return Response.of(request.getProtocol(), STATUS200, request.getUrl(), ContentType.from(request.getUrl()));
    }
}
