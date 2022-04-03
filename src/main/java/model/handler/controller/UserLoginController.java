package model.handler.controller;

import db.DataBase;
import model.handler.Handler;
import model.http.request.HttpServletRequest;
import model.http.response.HttpServletResponse;
import model.http.session.Cookie;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.SessionDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import static util.HttpRequestUtils.getPath;
import static util.Pathes.WEBAPP_ROOT;
import static util.SpecialCharacters.URL_DELIMETER;

public class UserLoginController implements Handler {

    private final Logger log = LoggerFactory.getLogger(UserLoginController.class);
    private static final UserLoginController instance = new UserLoginController();

    private UserLoginController() {
    }

    ;

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
        String requestUrl = request.getRequestURL();
        String path = getPath(WEBAPP_ROOT, requestUrl);

        String type = getExtention(requestUrl);
        byte[] body = Files.readAllBytes(new File(path).toPath());
        response.responseHeader(body.length, type);
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
        Cookie cookie = null;
        String httpRequestBody = request.getBody();
        Map<String, String> joinRequestParams = HttpRequestUtils.parseQueryString(httpRequestBody);
        User findUser = DataBase.login(joinRequestParams.get("userId"), joinRequestParams.get("password"));
        if (findUser == null) {
            redirectionURL = "/user/login_failed.html";
        } else {
            cookie = SessionDatabase.createCookie(findUser.getUserId());
            redirectionURL = "/index.html";
        }
        String path = getPath(WEBAPP_ROOT, redirectionURL);
        String extention = getExtention(requestURL);
        byte[] body = Files.readAllBytes(new File(path).toPath());
        if (cookie == null) {
            response.responseHeaderRedirection(body.length, extention, redirectionURL);
            response.responseBody(body);
            return;
        }
        response.responseHeaderLoginRedirection(body.length, extention, redirectionURL, cookie);
        response.responseBody(body);
    }

}
