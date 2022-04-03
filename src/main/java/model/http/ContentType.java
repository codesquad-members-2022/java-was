package model.http;

import java.util.Arrays;
import java.util.function.Supplier;

public enum ContentType {

    HTML("html", "text/html;"),
    CSS("css", "text/css;"),
    JAVASCRIPT("js", "javascript"),
    ICO("ico", "image/x-icon;"),
    WOFF("wof", "application/font-woff;"),
    OTHER("", "");

    private final String type;
    private final String mimeType;

    ContentType(String type, String mimeType) {
        this.type = type;
        this.mimeType = mimeType;
    }

    public static String of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.mimeType, inputType))
                .map(ContentType::getMimeType)
                .findAny()
                .orElseGet(none());
    }

    private static boolean euqalTo(String headerType, String inputType) {
        return headerType.equals(inputType);
    }

    public String getMimeType() {
        return mimeType;
    }

    private static Supplier<String> none() {
        return OTHER::getMimeType;
    }
}
