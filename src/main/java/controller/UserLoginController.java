package controller;

import db.DataBase;
import model.HttpStatus;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class UserLoginController implements Controller{
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> loginInfo = request.getParams();
        User user = DataBase.findUserById(loginInfo.get("userId"));
        response.setHttpStatus(HttpStatus.REDIRECT);
        
        if (user == null || !user.getPassword().equals(loginInfo.get("password"))) {
            response.setHeader("Location", "/user/login_failed.html");
            response.send();
        } else {
            response.setHeader("Location", "/index.html");
            response.setHeader("Set-Cookie", "sessionId=spring; path=/");
            response.send();
        }
    }
}
