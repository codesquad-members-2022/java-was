package db;

import java.util.HashMap;
import java.util.Map;

public class SessionDataBase {

    private static final Map<String, String> SESSIONS = new HashMap<>();

    public static void save(String sessionId, String userId) {
        SESSIONS.put(sessionId, userId);
    }

    public static void remove(String sessionId) {
        SESSIONS.remove(sessionId);
    }
}
