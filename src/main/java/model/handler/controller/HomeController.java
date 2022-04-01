package model.handler.controller;

import model.handler.Handler;
import model.request.HttpServletRequest;
import model.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;

public class HomeController implements Handler {

    private Logger log = LoggerFactory.getLogger(HomeController.class);

    private HomeController (){};
    private static final HomeController instance = new HomeController();

    public static HomeController getInstance() {
        if (instance == null) {
            return new HomeController();
        }
        return instance;
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        DataOutputStream dataOutputStream = response.getDataOutputStream();
        response(dataOutputStream);
    }

    private void response(DataOutputStream dataOutputStream) {

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
