package db;

import model.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    public static final String SESSION_ID = "sessionId";
    private static Map<String, User> sessionDB = new ConcurrentHashMap<>();

    public static String save(User value) {
        String sessionId = UUID.randomUUID().toString();
        sessionDB.put(sessionId, value);
        return sessionId;
    }

    public static void remove(String key) {
        sessionDB.remove(key);
    }

    public static boolean isLoginUser(String sessionId) {
        return sessionDB.containsKey(sessionId);
    }
}
