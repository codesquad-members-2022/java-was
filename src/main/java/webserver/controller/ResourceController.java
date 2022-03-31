package webserver.controller;

import webserver.ContentType;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceController implements Controller {
    private static final String STATUS200 = "200 OK ";

    @Override
    public Response handleRequest(Request request) {
        Response response = new Response(request.getProtocol(), STATUS200);
        response.saveBody(request.getUrl());

        return response;
    }
}
