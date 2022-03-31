package db;

import com.google.common.collect.Maps;
import java.util.Map;

public class SessionDataBase {

    private static Map<String, String> sessions = Maps.newHashMap();

    public static void add(String sessionId, String userId) {
        sessions.put(sessionId, userId);
    }

    public static String findBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }

}
