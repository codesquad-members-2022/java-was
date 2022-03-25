package db;

import webserver.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Sessions {

    private static final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public static HttpSession getSession(String sessionId) {
        HttpSession session;

        if ((session = sessions.get(sessionId)) != null) {
            return session;
        }

        session = new HttpSession(sessionId);
        sessions.put(sessionId, session);
        return session;
    }

    public static void remove(String sessionId) {
        getSession(sessionId).invalidate();
        sessions.remove(sessionId);
    }
}
