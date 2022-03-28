package servlet;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import util.HttpRequestUtils;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LoginServlet extends BaseServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        response.forward("/user/login.html");
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String body = request.getBody();
        String queryString = HttpRequestUtils.getQueryString(body);
        Map<String, String> parsedMap = HttpRequestUtils.parseQueryString(queryString);
        String userId = parsedMap.get("userId");
        String password = parsedMap.get("password");
        Optional<User> findUser = DataBase.findUserById(userId);
        if (findUser.isPresent() && findUser.get().isSamePassword(password)) {
            String sessionId = UUID.randomUUID().toString();
            response.setHeader("Set-Cookie", sessionId);
            response.redirection("/index.html");
        }
        response.redirection("/user/login_failed.html");

    }
}
