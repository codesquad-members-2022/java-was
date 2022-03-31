package webserver.login;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDataBase {
    private static final Map<String, Cookie> cookies = new ConcurrentHashMap<>();

    private SessionDataBase() {

    }

    public static void addCookie(Cookie cookie) {
        cookies.put(cookie.getSessionId(), cookie);
    }

    public static void deleteCookie(String sessionId) {
        cookies.remove(sessionId);
    }

    public static boolean isLoggedIn(String sessionId) {
        return cookies.containsKey(sessionId);
    }
}
