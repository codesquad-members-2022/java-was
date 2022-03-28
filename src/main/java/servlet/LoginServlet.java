package servlet;

import db.DataBase;
import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import webserver.RequestHandler;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class LoginServlet extends BaseServlet {
    private static final Logger log = LoggerFactory.getLogger(LoginServlet.class);

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
        log.debug("[User Status] : {}, {}", userId, password);
        Optional<User> findUser = DataBase.findUserById(userId);

        if (findUser.isPresent() && findUser.get().isSamePassword(password)) {
            String sessionId = UUID.randomUUID().toString();
            log.debug("[SessionId] : {}", sessionId);
            Session.save(sessionId, findUser.get());
            response.addHeader("Set-Cookie", sessionId + "; Path=/");
            response.redirection("/index.html");
            return;
        }

        log.debug("여기 로그인 !!!!!!!!!!!!!!");
        response.redirection("/user/login_failed.html");
    }
}
