package webserver;

import db.DataBase;
import db.SessionDataBase;
import java.util.Map;
import java.util.UUID;
import model.User;
import util.HttpRequestUtils;

public class UserManager {

    private final Request request;

    public UserManager(Request request) {
        this.request = request;
    }

    private void save(URL url, String messageBody) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(messageBody);
        User user = new User(userInfo.get("userId"), userInfo.get("password"),
            userInfo.get("name"),
            userInfo.get("email"));

        if (DataBase.findUserById(userInfo.get("userId")) == null) {
            DataBase.addUser(user);
            url.changePathToHomePage();
        } else {
            url.changePathToSignUpPage();
        }
    }

    private String login(URL url, String messageBody) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(messageBody);
        User user = DataBase.findUserById(userInfo.get("userId"));

        if (user.getUserId().equals(userInfo.get("userId")) && user.getPassword()
            .equals(userInfo.get("password"))) {
            String sessionId = UUID.randomUUID().toString();
            SessionDataBase.add(sessionId, userInfo.get("userId"));

            url.changePathToHomePage();
            return sessionId;
        } else {
            url.changePathToLoginFailedPage();
            return "";
        }
    }

    private String logout(URL url, Map<String, String> headers) {
        String value = headers.get("Cookie:");
        String sessionId = value.split("=")[1];

        SessionDataBase.remove(sessionId);
        url.changePathToHomePage();

        return sessionId;
    }

    public String action() {
        URL url = request.getUrl();
        String messageBody = request.getMessageBody();

        if (request.isPostMethodType() && url.comparePath("/user/create")) {
            save(url, messageBody);
        }
        if (request.isPostMethodType() && url.comparePath("/user/login")) {
            return login(url, messageBody);
        }
        if (request.isGetMethodType() && url.comparePath("/user/logout")) {
            Map<String, String> headers = request.getHeaders();
            return logout(url, headers);
        }

        return "";
    }
}
