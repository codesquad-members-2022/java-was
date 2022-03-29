package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.request.Request;
import webserver.response.Response;
import java.util.Map;

public class UserLoginController implements Controller {

    private static final String STATUS302 = "302 Redirect ";

    private final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Override
    public Response handleRequest(Request request) {
        String viewPath = login(request.getRequestBody());

        return Response.of(request.getProtocol(), STATUS302, viewPath);
    }

    private String login(String requestBody) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);

        String userId = params.get("userId");
        User findUser = DataBase.findUserById(userId)
                .orElseThrow(IllegalArgumentException::new);

        String password = params.get("password");

        if (!findUser.isSamePassword(password)) {
            log.debug("login failed {}", userId);
            return "/user/login_failed.html";
        }

        log.debug("login Success {}", userId);

        return "/index.html";
    }
}
