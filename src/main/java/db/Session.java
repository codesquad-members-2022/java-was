package db;

import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    public static final String SESSION_ID = "sessionId";
    private static Map<String, User> sessionDB = new ConcurrentHashMap<>();

    public static void save(String key, User value) {
        sessionDB.put(key, value);
    }

    public static void remove(String key) {
        sessionDB.remove(key);
    }
}
