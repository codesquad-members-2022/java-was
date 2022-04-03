package model.handler.controller;

public enum CookieStatus {
    VALID,
    INVALID;

    public boolean isValidCookie() {
        return this.equals(VALID);
    }
}
