package model.handler.controller;

import model.handler.Handler;
import model.request.HttpServletRequest;
import model.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class UserLoginController implements Handler {

    private final Logger log = LoggerFactory.getLogger(UserLoginController.class);
    private static final UserLoginController instance = new UserLoginController();

    private UserLoginController() {};

    public static UserLoginController getInstance() {
        if (instance == null) {
            return new UserLoginController();
        }
        return instance;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.isGet()) {
            doGet(request, response);
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
