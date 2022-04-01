package controller;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(DefaultController.class);
    private static final DefaultController instance = new DefaultController();

    public static DefaultController getInstance() {
        return instance;
    }

    private DefaultController() {

    }

    @Override
    public Response run(Request request) throws IOException {
        String url = request.methodUrl().getValue(); // /index.html
        String contentType = url.split("\\.")[1];
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        Map<String, String> responseHeader = new HashMap<>();
//        responseHeader.put("Content-Type", "text/" + contentType + ";charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(body.length));
        Response response = new Response("HTTP/1.1", "200", "OK", responseHeader, body);
        log.debug("[response] : {}", response.responseMessage());
        return response;
    }
}
