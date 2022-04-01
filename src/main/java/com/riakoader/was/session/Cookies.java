package com.riakoader.was.session;

import java.util.ArrayList;
import java.util.List;

public class Cookies {

    private final List<Cookie> cookies = new ArrayList<>();

    public void add(Cookie cookie) {
        cookies.add(cookie);
    }

    public String toSetCookieMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Cookie cookie : cookies) {
            stringBuilder.append("Set-Cookie: ").append(cookie).append(System.lineSeparator());
        }
        return stringBuilder.toString();
    }
}
