package webserver.http;

import model.User;

public class Cookie {

    private final static String DIVIDER = "; ";

    private String userId;
    private String path;
    private String expireDateTime;

    public Cookie(User user) {
        this.userId = user.getUserId();
        this.path = "path=/; ";
        generateExpireDate();
    }

    public String getCookieString(String sessionId) {
        StringBuilder sb = new StringBuilder();
        sb.append("sessionId="+sessionId);
        sb.append(DIVIDER);
        sb.append(path);
        sb.append(expireDateTime);

        return sb.toString();
    }

    public void expire() {
        this.expireDateTime = "Expires=Mon, 28-Mar-1970 06:41:18 GMT;";
    }

    private void generateExpireDate() {
        this.expireDateTime = "Expires=Mon, 28-Mar-2025 06:41:18 GMT;";
//                LocalDateTime.now().plusHours(1)
//                .format(DateTimeFormatter.ofPattern(String.valueOf(DateTimeFormatter.RFC_1123_DATE_TIME)));
//        return "Expires=" + expireDateTime + DIVIDER;
    }



}
