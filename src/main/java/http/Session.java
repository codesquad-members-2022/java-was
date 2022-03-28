package http;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Session {

    private static final AtomicInteger sessionIdGenerator = new AtomicInteger(1);
    private static final Map<Integer, Map<String, Object>> sessionMap = new ConcurrentHashMap<>();

    private Session() { }

    public static int createSession() {
        HashMap<String, Object> sessionAttribute = new HashMap<>();
        int sessionId = sessionIdGenerator.getAndIncrement();
        sessionMap.put(sessionId, sessionAttribute);

        return sessionId;
    }

    public static void setAttribute(int sessionId, String key, Object value) {
        Map<String, Object> sessionAttribute = sessionMap.get(sessionId);
        sessionAttribute.put(key, value);
    }

    public static Object getAttribute(int sessionId, String key) {
        Map<String, Object> sessionAttribute = sessionMap.get(sessionId);
        return sessionAttribute.get(key);
    }

    public static boolean isSessionIdExist(int id) {
        return sessionMap.containsKey(id);
    }

    public static void invalidateSession(int id) {
        sessionMap.remove(id);
    }


}
