package model.handler.controller;

import java.time.LocalDateTime;

public class Cookie {

    private static final String BASIC_PATH = "/";
    private static final String INVALID_COOKIE = "유효하지 않은 쿠키입니다.";
    private static final String CHANGED_COOKIE = "값이 변경된 쿠키입니다.";
    private static final String EXPIRED_COOKIE = "기한이 만료된 쿠키입니다.";

    private final String UUID;
    private String value;
    private String path;
    private String name;
    private int version;

    private CookieStatus cookieStatus;
    private CookiePeriod cookiePeriod;

    public Cookie(String name, String value) {
        this.UUID = getUUID();
        this.name = name;
        this.value = value;
        init();
    }

    private void init() {
        this.path = BASIC_PATH;
        this.cookieStatus = CookieStatus.VALID;
        this.cookiePeriod = new CookiePeriod();
    }

    private String getUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    private void validateCookie(String UUID) {
        if (isInValidCookie()) {
            this.cookieStatus = CookieStatus.INVALID;
            throw new IllegalStateException(INVALID_COOKIE);
        }
        if (isExpired()) {
            this.cookieStatus = CookieStatus.INVALID;
            throw new IllegalStateException(EXPIRED_COOKIE);
        }
        if (isChanged(UUID)) {
            this.cookieStatus = CookieStatus.INVALID;
            throw new IllegalStateException(CHANGED_COOKIE);
        }
    }

    public boolean isExpired() {
        return cookiePeriod.isExpired(LocalDateTime.now());
    }

    private boolean isChanged(String UUID) {
        return !this.UUID.equals(UUID);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isInValidCookie() {
        return !cookieStatus.isValidCookie();
    }
}
