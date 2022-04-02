package model.http;

import java.util.Arrays;
import java.util.function.Supplier;

public enum ContentType {

    HTML("html", "text/html;"),
    CSS("css", "text/css;"),
    NONE("", "");

    private final String extension;
    private final String mimeType;

    ContentType(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static String of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.mimeType, inputType))
                .map(ContentType::getType)
                .findAny()
                .orElseGet(none());
    }

    private static boolean euqalTo(String headerType, String inputType) {
        return headerType.equals(inputType);
    }

    public String getType() {
        return mimeType;
    }

    private static Supplier<String> none() {
        return NONE::getType;
    }
}
