package com.riakoader.was.session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cookies {

    private final List<Cookie> cookies = new ArrayList<>();

    public void add(Cookie cookie) {
        cookies.add(cookie);
    }

    public String toSetCookieMessage() {
        return cookies.stream().map(cookie -> "Set-Cookie: " + cookie).collect(Collectors.joining());
    }
}
