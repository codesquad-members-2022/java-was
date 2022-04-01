package controller;

import http.Request;
import http.Response;

import java.io.IOException;

public interface Controller {
    public Response run(Request request) throws IOException;
}
