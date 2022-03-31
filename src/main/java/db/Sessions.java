package db;

import webserver.HttpSession;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Sessions {

    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public static HttpSession getSession(String sessionId) {
        return Optional.ofNullable(sessions.get(sessionId))
                .orElseGet(
                        () -> sessions.put(sessionId, new HttpSession(sessionId))
                );
    }

    public static void remove(String sessionId) {
        getSession(sessionId).invalidate();
        sessions.remove(sessionId);
    }
}
