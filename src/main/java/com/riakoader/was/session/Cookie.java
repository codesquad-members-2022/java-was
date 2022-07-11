package com.riakoader.was.session;

public class Cookie {

    private final String name;

    private final String value;

    private int maxAge = -1;

    private String path;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void setMaxAge(int expiry) {
        this.maxAge = expiry;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setPath(String uri) {
        this.path = uri;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name + "=" + value + "; " + (maxAge < 0 ? "" : "Max-Age=" + maxAge + "; ") + "Path=" + path;
    }
}
