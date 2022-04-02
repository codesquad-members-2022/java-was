package webserver.mapper;

import model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Session {

    private static Session session;

    private Session() {
    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    private Map<String, User> userMap = new HashMap<>();

    public User getUser(String sessionId) {
        return userMap.get(sessionId);
    }

    public String setUser(User user) {
        String sessionId = UUID.randomUUID().toString();
        userMap.put(sessionId, user);
        return sessionId;
    }
}
