package webserver.login;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cookie {
    private String sessionId;

    public Cookie(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCookieString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name=")
                .append(sessionId)
                .append("; path=/");

        return sb.toString();
    }

    public String getSessionId() {
        return sessionId;
    }

    private String getExpireDate() {
        String date = LocalDateTime.now().plusHours(1)
                .format(DateTimeFormatter.ofPattern(String.valueOf(DateTimeFormatter.RFC_1123_DATE_TIME)));
        return "expires=" + date + ";";
    }
}
