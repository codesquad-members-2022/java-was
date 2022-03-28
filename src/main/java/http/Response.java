package http;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Response {
    private final Map<String, String> header = new HashMap<>();
    private HttpStatus httpStatus;
    private String redirectUrl;

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
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
}
