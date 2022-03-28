package model.http;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum HeaderType {

    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    REQUEST_METHOD("Request Method"),
    REQUEST_URL("Request URL"),
    CACHE_CONTROL("Cache-Control"),
    PORT("Port"),
    CONNECTION("Connection"),
    SEC_CH_UA_PLATFORM("sec-ch-ua-platform"),
    SEC_FETCH_MODE("Sec-Fetch-Mode"),
    SEC_CH_UA_MOBILE("sec-ch-ua-mobile"),
    SEC_FETCH_USER("Sec-Fetch-User"),
    SEC_CH_UA("sec-ch-ua"),
    USER_AGENT("User-Agent"),
    PURPOSE("Purpose"),
    SEC_FETCH_DEST("Sec-Fetch-Dest"),
    HOST("Host"),
    ACCEPT("Accept"),
    SEC_FETCH_SITE("Sec-Fetch-Site"),
    ACCEPT_ENCODING("Accept-Encoding"),
    UPGRADE_INSECURE_REQUESTS("Upgrade-Insecure-Requests"),
    COOKIE("Cookie"),
    ACCEPT_LANGUAGE("Accept-Language"),
    REFERER("Referer"),
    ORIGIN("Origin");

    private final String value;

    HeaderType(String value) {
        this.value = value;
    }

    public static HeaderType of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.value, inputType))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 헤더는 존재하지 않습니다."));
    }

    public static List<String> getHeaderTypes() {
        return Arrays.stream(values())
                .map(HeaderType::getValue)
                .collect(Collectors.toUnmodifiableList());
    }

    private static boolean euqalTo(String headerType, String inputType) {
        return headerType.equals(inputType);
    }

    public String getValue() {
        return value;
    }
}
