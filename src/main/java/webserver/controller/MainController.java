package webserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpResponse;
import java.io.IOException;

public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    public HttpResponse main(String url, HttpResponse httpResponse) {
        return httpResponse.ok(url);
    }
}
