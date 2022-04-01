package model.handler.controller;

import model.handler.Handler;
import model.request.HttpServletRequest;
import model.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserCreateController implements Handler {

    private final Logger log = LoggerFactory.getLogger(UserCreateController.class);
    private static final UserCreateController instance = new UserCreateController();
    private UserCreateController (){};

    public static UserCreateController getInstance() {
        if (instance == null) {
            return new UserCreateController();
        }
        return instance;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.isGet()) {
            doGet(request, response);
            return;
        }
        if (request.isPost()) {
            doPost(request, response);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}
