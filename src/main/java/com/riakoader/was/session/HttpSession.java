package com.riakoader.was.session;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

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

    private final Map<String, String> session = new HashMap<>();

    public String getAttribute(String name) {
        return session.get(name);
    }

    public void setAttribute(String name, String value) {
        session.put(name, value);
    }

    public void removeAttribute(String name) {
        session.remove(name);
    }
}
