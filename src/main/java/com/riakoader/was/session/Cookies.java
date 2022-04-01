package com.riakoader.was.session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cookies {

    private final List<Cookie> cookies = new ArrayList<>();

    public void add(Cookie cookie) {
        cookies.add(cookie);
    }

    public boolean isEmpty() {
        return cookies.isEmpty();
    }

    @Override
    public String toString() {
        return cookies.stream().map(Cookie::toString).collect(Collectors.joining());
    }
}
