package http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Response {
    private final static String LOCATION_HEADER = "Location";

    private final Map<String, String> header = new HashMap<>();
    private final List<Cookie> cookies = new ArrayList<>();

    private HttpStatus httpStatus;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getRedirectUrl() {
        return header.get(LOCATION_HEADER);
    }

    public void setRedirectUrl(String redirectUrl) {
        this.httpStatus = HttpStatus.FOUND;
        header.put(LOCATION_HEADER, redirectUrl);
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public String findHeader(String key) {
        return header.get(key);
    }

    public Set<String> headerKeySet() {
        return header.keySet();
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public List<Cookie> getCookies() {
        return Collections.unmodifiableList(cookies);
    }
}
