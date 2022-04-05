package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

@FunctionalInterface
public interface BackController {

    Response process(Request request);
}
