package model.http;

import java.util.Arrays;
import java.util.function.Supplier;

public enum Extention {

    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    NONE("", "");

    private final String extension;
    private final String mimeType;
    Extention(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    public static String of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.mimeType, inputType))
                .map(Extention::getType)
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
