package model.handler.controller;

import model.handler.Handler;
import model.http.request.HttpServletRequest;
import model.http.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.nio.file.Files;

import static util.HttpRequestUtils.getPath;
import static util.Pathes.WEBAPP_ROOT;

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
        DataOutputStream dos = dataOutputStream;
//
//        String requestUrl = httpServletRequest.getRequestURL();
//        String path = getPath(WEBAPP_ROOT, requestUrl);
//
//        String type = getExtention(requestUrl);
//        byte[] body = Files.readAllBytes(new File(path).toPath());
//        System.out.println(type);
//        responseHeader(dos, body.length, type);
//        responseBody(dos, body);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {

    }
}
