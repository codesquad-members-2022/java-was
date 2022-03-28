package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    public HttpResponse main(String url) {
        HttpResponse response = new HttpResponse(request.getVersion(), HttpStatus.OK);
        try {
            response.addBody(Files.readAllBytes(new File("./webapp" + url).toPath()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return response;
    }
}
