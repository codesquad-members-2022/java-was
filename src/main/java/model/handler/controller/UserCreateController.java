package model.handler.controller;

import db.DataBase;
import model.handler.Handler;
import model.http.request.HttpServletRequest;
import model.http.response.HttpServletResponse;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpRequestUtils.getPath;
import static util.Pathes.WEBAPP_ROOT;
import static util.SpecialCharacters.URL_DELIMETER;

public class UserCreateController implements Handler {

    private final Logger log = LoggerFactory.getLogger(UserCreateController.class);
    private static final UserCreateController instance = new UserCreateController();

    private UserCreateController() {
    }

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
        DataOutputStream dos = response.getDataOutputStream();

        String requestURL = request.getRequestURL();
        String path = getPath(WEBAPP_ROOT, requestURL);

        String extention = getExtention(requestURL);
        byte[] body = Files.readAllBytes(new File(path).toPath());

        response.responseHeader(body.length, extention);
        response.responseBody(body);
    }

    private String getExtention(String requestUrl) {
        String[] extentionArray = requestUrl.split(URL_DELIMETER);
        return extentionArray[extentionArray.length - 1];
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestURL = request.getRequestURL();
        String redirectionURL;
        String httpRequestBody = request.getBody();
        Map<String, String> joinRequestParams = HttpRequestUtils.parseQueryString(httpRequestBody);
        User user = new User(joinRequestParams.get("userId"), joinRequestParams.get("password"), URLDecoder.decode(joinRequestParams.get("name"), StandardCharsets.UTF_8), joinRequestParams.get("email"));
        redirectionURL = "/index.html";
        DataBase.addUser(user);
        String path = getPath(WEBAPP_ROOT, redirectionURL);

        String[] extentionArray = requestURL.split(URL_DELIMETER);
        String extention = extentionArray[extentionArray.length - 1];

        byte[] body = Files.readAllBytes(new File(path).toPath());

        response.responseHeaderRedirection(body.length, extention, redirectionURL);
        response.responseBody(body);
    }
}
