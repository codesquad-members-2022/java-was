package webserver.http;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Cookie {
    private String name;
    private String value;
    private String path;
    private String expire;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getCookieString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("=");
        sb.append(value);
        sb.append("; ");
        sb.append("path=/");
//        sb.append(getExpireDate());

        return sb.toString();
    }

    private String getExpireDate() {
        String date = LocalDateTime.now().plusHours(1)
                .format(DateTimeFormatter.ofPattern(String.valueOf(DateTimeFormatter.RFC_1123_DATE_TIME)));
        return "expires=" + date + ";";
    }


}
