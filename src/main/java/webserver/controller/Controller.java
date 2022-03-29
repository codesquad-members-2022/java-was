package webserver.controller;

import java.io.IOException;

import webserver.http.Request;
import webserver.http.Response;

public interface Controller {
    void process(Request request, Response response) throws IOException;
}
