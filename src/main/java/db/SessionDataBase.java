package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;
import java.util.UUID;

public class SessionDataBase {

    private static final Map<String, User> sessions = Maps.newHashMap();

    public static String saveSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    public static boolean findSessionByUser(String sessionId) {
        return sessions.get(sessionId) != null;
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }
}
