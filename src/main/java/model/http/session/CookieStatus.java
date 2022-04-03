package model.http.session;

public enum CookieStatus {
    VALID,
    INVALID;

    public boolean isValidCookie() {
        return this.equals(VALID);
    }
}
