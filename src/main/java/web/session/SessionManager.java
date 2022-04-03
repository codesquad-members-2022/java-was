package web.session;

import model.User;
import web.common.Cookie;
import web.response.HttpResponse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private final static Map<String, User> storage = new ConcurrentHashMap<>();

    public static final String SESSION_COOKIE_NAME = "sessionId";

    public static void createSession(User user, HttpResponse response) {
        String sessionId = UUID.randomUUID().toString();
        storage.put(sessionId, user);
        Cookie cookie = new Cookie(SESSION_COOKIE_NAME,sessionId);
        response.addCookie(cookie);
    }

    public static void expireSession(String sessionId) {
        storage.remove(sessionId);
    }
}
