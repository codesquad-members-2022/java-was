package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUpController implements Controller {

    private static final Logger log = LoggerFactory.getLogger(SignUpController.class);
    private static final SignUpController instance = new SignUpController();

    public static SignUpController getInstance() {
        return instance;
    }

    private SignUpController() {

    }

    @Override
    public Response run(Request request) {
        byte[] body = "".getBytes();
        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", "text/html;charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(body.length));

        DataBase.findUserById(request.findBodyByFieldName("userId"))
                .ifPresentOrElse(user -> {
                    responseHeader.put("Location", "/user/form.html");
                    log.debug("중복된 아이디가 존재합니다.");
                }, () -> {
                    User user = new User(
                            request.findBodyByFieldName("userId"),
                            request.findBodyByFieldName("password"),
                            request.findBodyByFieldName("name"),
                            request.findBodyByFieldName("email"));
                    responseHeader.put("Location", "/index.html");
                    log.debug("User : {}", user);
                    DataBase.addUser(user);
                });
        Response response = new Response("HTTP/1.1", "302", "Found", responseHeader, body);
        log.debug("[response] : {}", response.responseMessage());
        return response;
    }
}
