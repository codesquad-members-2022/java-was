package controller;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class LogInController implements Controller {
    private static final Logger log = LoggerFactory.getLogger(LogInController.class);
    private static final LogInController instance = new LogInController();

    public static LogInController getInstance() {
        return instance;
    }

    private LogInController() {

    }

    @Override
    public Response run(Request request) {
        String userId = request.findBodyByFieldName("userId");
        String password = request.findBodyByFieldName("password");
        Optional<User> user = DataBase.findUserById(userId);

        Map<String, String> responseHeader = new HashMap<>();
        responseHeader.put("Content-Type", "text/html;charset=utf-8");
        responseHeader.put("Content-Length", String.valueOf(0));
        if (user.isEmpty() || !user.get().isCorrectPassword(password)) {
            log.debug("로그인을 실패하였습니다!");
            responseHeader.put("Location", "/user/login_failed.html");

        } else if (user.get().isCorrectPassword(password)) {
            log.debug("로그인을 성공했습니다!");
            responseHeader.put("Location", "/index.html");
            responseHeader.put("Set-Cookie", "logined=true; Path=/");
        }
        Response response = new Response("HTTP/1.1", "302", "Found", responseHeader, "".getBytes());
        log.debug("[response] : {}", response.responseMessage());
        return response;
    }
}
