package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

@FunctionalInterface
public interface Controller {

    Response handleRequest(Request request);
}
