package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DefaultController extends Controller{

    public DefaultController(Request request, Response response) {
        super(request, response);
    }

    @Override
    public void service() throws IOException {
        byte[] body = null;
        body = Files.readAllBytes(new File("./webapp/" + request.getUri()).toPath());
        response.setBody(body, "text/html");
    }
}
