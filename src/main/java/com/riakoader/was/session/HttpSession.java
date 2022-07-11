package com.riakoader.was.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpSession {

    private final Map<String, Object> session = new ConcurrentHashMap<>();

    private static volatile HttpSession httpSession;

    private HttpSession() {
    }

    public static HttpSession getInstance() {
        if (httpSession == null) {
            synchronized (HttpSession.class) {
                if (httpSession == null) {
                    httpSession = new HttpSession();
                }
            }
        }
        return httpSession;
    }

    public Object getAttribute(String name) {
        return session.get(name);
    }

    public void setAttribute(String name, Object value) {
        session.put(name, value);
    }

    public void removeAttribute(String name) {
        session.remove(name);
    }
}
