package controller;

import db.DataBase;
import model.HttpStatus;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class UserCreateController implements Controller{
    @Override
    public void service(HttpRequest request, HttpResponse response) throws IOException {
        Map<String, String> userInfo = request.getParams();
        try {
            DataBase.addUser(User.from(userInfo));
            response.setHttpStatus(HttpStatus.REDIRECT);
            response.setHeader("Location", "/index.html");
            response.send();

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
            response.send();
        }
    }
}
