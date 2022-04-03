package webserver;

import configuration.ObjectFactory;
import model.handler.controller.Cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static Map<String, Object> sessionDatabase;
    private static SessionValidator validator;
    private static final SessionDatabase instance = new SessionDatabase();

    private SessionDatabase() {
        sessionDatabase = new ConcurrentHashMap<>();
        validator = ObjectFactory.sessionValidator;
    }

    public static SessionDatabase getInstance() {
        if (instance == null) {
            return new SessionDatabase();
        }
        return instance;
    }

    public static Cookie createCookie(String id) {
        Cookie cookie = new Cookie("id", id);
        sessionDatabase.put(id, cookie);
        return cookie;
    }

    public static void put(String sessionId, Object session) {
        sessionDatabase.put(sessionId, session);
    }

    public static void remove(String sessionId) {
        sessionDatabase.remove(sessionId);
    }

    public static boolean isExpired(Cookie cookie) {
        return cookie.isExpired();
    }
}
