package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private static final String INDEX_PAGE_URL = "/index.html";

    public HttpResponse main(HttpRequest request) {
        HttpResponse response = new HttpResponse(request.getVersion(), HttpStatus.OK);
        try {
            response.addBody(Files.readAllBytes(new File("./webapp" + INDEX_PAGE_URL).toPath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }
}
