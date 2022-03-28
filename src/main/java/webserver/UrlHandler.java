package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class UrlHandler {
    private static final Logger log = LoggerFactory.getLogger(UrlHandler.class);
    private static final UrlHandler instance = new UrlHandler();
    private Map<String, Function<String, User>> urlMapper = new HashMap<>(Map.of(
        "/user/create", this::addUserInfo)
    );

    private UrlHandler() {
    }

    public static UrlHandler getInstance() {
        return instance;
    }

    /// user/create?userId=javajigi&password=password&~
    public void mapUrl(String url) {
        String[] requestLine = url.split("\\?");
        Optional.ofNullable(urlMapper.get(requestLine[0]))
            .ifPresent(function -> function.apply(url));
    }

    private User addUserInfo(String url) {
        String[] requestLine = url.split("\\?");
        Map<String, String> userData = HttpRequestUtils.parseQueryString(requestLine[1]);

        User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"),
            userData.get("email"));

        log.debug("user = {}", user);
        DataBase.addUser(user);
        return user;
    }

}
