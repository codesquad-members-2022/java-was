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
        Response response = Response.of(request.getProtocol(), STATUS302);

        return signUp(request, response);
    }

    private Response signUp(Request request, Response response) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getRequestBody());
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));

        String userId = user.getUserId();

        if (DataBase.findUserById(userId).isPresent()) {
            log.debug("{} sign up failed", userId);
            response.setLocation("/user/form.html");
            return response;
        }

        DataBase.addUser(user);
        log.debug("{} sign up", user);

        response.setLocation("/index.html");
        return response;
    }
}
