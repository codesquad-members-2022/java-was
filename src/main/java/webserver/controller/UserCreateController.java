package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;

public class UserCreateController implements Controller {

    private static final String STATUS302 = "302 Redirect ";

    private final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    @Override
    public Response handleRequest(Request request) {
        String viewPath = signUp(request.getRequestBody());

        return Response.of(request.getProtocol(), STATUS302, viewPath);
    }

    private String signUp(String requestBody) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        log.debug("{} sign up", user);

        return "/index.html";
    }
}
