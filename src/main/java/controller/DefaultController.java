package controller;

import model.ContentType;
import model.HttpStatus;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DefaultController implements Controller {
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        response.setHttpStatus(HttpStatus.OK);

        if (path.endsWith("html")) {
            response.setHeader("Content-Type", ContentType.HTML.getType());
        } else if (path.endsWith("css")) {
            response.setHeader("Content-Type", ContentType.CSS.getType());
        } else if (path.endsWith("js")) {
            response.setHeader("Content-Type", ContentType.JS.getType());
        }
        response.sendWithBody(body);
    }
}
