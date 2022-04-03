package webserver;

import configuration.ObjectFactory;
import model.http.session.Cookie;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Todo 요구사항에 맞게 변수명 정리
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

    public static Cookie createCookie(String sessionId) {
        Cookie cookie = new Cookie("sessionId", sessionId);
        sessionDatabase.put(sessionId, cookie);
        return cookie;
    }

    public Cookie getCookie(String sessionId) {
        return (Cookie) sessionDatabase.get(sessionId);
    }

    public int size() {
        return sessionDatabase.size();
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
