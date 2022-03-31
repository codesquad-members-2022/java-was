package webserver.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import webserver.http.Request;
import webserver.http.Response;

public class DefaultController implements Controller {
    @Override
    public void process(Request request, Response response) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + request.getUrl()).toPath());
        response.response200Header(body.length, request.getUrl());
        response.responseBody(body);
    }
}
