package webserver.controller;

import db.DataBase;
import db.SessionDataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.request.Request;
import webserver.response.Response;
import java.util.Map;
import java.util.Optional;

public class UserLoginController implements Controller {

    private static final String STATUS302 = "302 Redirect ";

    private final Logger log = LoggerFactory.getLogger(UserLoginController.class);

    @Override
    public Response handleRequest(Request request) {
        Response response = Response.of(request.getProtocol(), STATUS302);
        return login(request, response);
    }

    private Response login(Request request, Response response) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(request.getRequestBody());

        String userId = params.get("userId");
        String password = params.get("password");
        Optional<User> userOptional = DataBase.findUserById(userId);


        if(userOptional.isEmpty()) {
            response.setLocation("/user/login_failed.html");
            return response;
        }

        User findUser = userOptional.get();
        if (!findUser.isSamePassword(password)) {
            response.setLocation("/user/login_failed.html");
            return response;
        }

        String sessionId = SessionDataBase.saveSession(findUser);
        response.setCookie("sessionId", sessionId);
        log.debug("login Success {}", userId);
        response.setLocation("/index.html");
        return response;

    }
}
