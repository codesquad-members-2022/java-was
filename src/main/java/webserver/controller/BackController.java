package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

public interface BackController {

    Response process(Request request);
}
