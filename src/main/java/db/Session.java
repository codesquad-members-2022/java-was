package db;

import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static Map<String, User> sessionDB = new ConcurrentHashMap<>();

    public static void save(String key, User value) {
        sessionDB.put(key, value);
    }
}
