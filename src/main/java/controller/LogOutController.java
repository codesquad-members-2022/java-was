package controller;

import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class LogOutController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(LogOutController.class);
    private static final LogOutController instance = new LogOutController();

    public static LogOutController getInstance() {
        return instance;
    }

    private LogOutController() {

    }

    @Override
    public Response run(Request request) throws IOException {
        byte[] body;
        body = Files.readAllBytes(new File("./webapp" + "/index.html").toPath());
        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", "text/html;charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(body.length));
        responseHeader.put("Location", "/index.html");
        responseHeader.put("Set-Cookie", "logined=true; Max-Age=0; Path=/");
        Response response = new Response("HTTP/1.1", "302", "Found", responseHeader, body);
        log.debug("[response] : {}", response.responseMessage());
        return response;
    }
}
