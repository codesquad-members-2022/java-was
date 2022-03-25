package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.IOException;

public abstract class Controller {
    Request request;
    Response response;

    protected Controller(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public void service() throws IOException {
    }
}
