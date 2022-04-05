package webserver.controller;

import webserver.ContentType;
import webserver.request.Request;
import webserver.response.Response;

public class ResourceController implements BackController {
    private static final String STATUS200 = "200 OK";

    @Override
    public Response process(Request request) {
        Response response = new Response(request.getProtocol(), STATUS200);
        response.saveBody(request.getUrl());

        return response;
    }
}
