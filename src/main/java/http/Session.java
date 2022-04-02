package http;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {

    private static final Map<String, Map<String, Object>> sessionMap = new ConcurrentHashMap<>();

    private Session() { }

    public static String generateSessionId() {
        HashMap<String, Object> sessionAttribute = new HashMap<>();
        String sessionId = UUID.randomUUID().toString();
        sessionMap.put(sessionId, sessionAttribute);
        return sessionId;
    }

    public static void setAttribute(String sessionId, String key, Object value) {
        Map<String, Object> sessionAttribute = sessionMap.get(sessionId);
        sessionAttribute.put(key, value);
    }

    public static Object getAttribute(String sessionId, String key) {
        Map<String, Object> sessionAttribute = sessionMap.get(sessionId);
        return sessionAttribute.get(key);
    }

    public static boolean isSessionIdExist(String id) {
        return sessionMap.containsKey(id);
    }

    public static void invalidateSession(String id) {
        sessionMap.remove(id);
    }


}
