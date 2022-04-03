package model.http.session;

import java.time.LocalDateTime;
import java.util.Objects;

public class Cookie {

    private static final String BASIC_PATH = "/";
    private static final String VALUE_EQUAL = "=";
    private static final String COOKIE_VALUE_DELIMETER = "; ";
    private static final String MAX_AGE = "Max-Age";
    private static final String PATH = "Path";
    private static final String INVALID_COOKIE = "유효하지 않은 쿠키입니다.";
    private static final String CHANGED_COOKIE = "값이 변경된 쿠키입니다.";
    private static final String EXPIRED_COOKIE = "기한이 만료된 쿠키입니다.";

    /**
     * 값 여러개 처리
     */
    private final String UUID;
    private String value;
    private String path;
    private String name;

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

    public long getMaxAge() {
        return cookiePeriod.getMaxAge();
    }

    public long setMaxAge(long time){
        cookiePeriod.setMaxAge(time);
        return time;
    }

    public boolean isInValidCookie() {
        return !cookieStatus.isValidCookie();
    }

    @Override
    public String toString() {
        return name + VALUE_EQUAL + value + COOKIE_VALUE_DELIMETER + PATH + VALUE_EQUAL + BASIC_PATH + COOKIE_VALUE_DELIMETER + MAX_AGE + VALUE_EQUAL + getMaxAge() + ";";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cookie cookie = (Cookie) o;
        return Objects.equals(UUID, cookie.UUID) && Objects.equals(value, cookie.value) && cookieStatus == cookie.cookieStatus && Objects.equals(cookiePeriod, cookie.cookiePeriod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UUID, value, cookieStatus, cookiePeriod);
    }
}
