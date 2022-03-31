package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import webserver.Request;

public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    public HttpResponse main(Request request, HttpResponse httpResponse) {
        return httpResponse.ok(request.getPath());
    }
}
