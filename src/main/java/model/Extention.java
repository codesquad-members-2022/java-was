package model;

import java.util.Arrays;
import java.util.function.Supplier;

public enum Extention {

    HTML("html", "text/html;charset=utf-8", "html관련 확장자"),
    CSS("css", "text/css;charset=utf-8", "css관련 확장자"),
    NONE("", "", "");

    private final String extension;
    private final String type;
    private final String description;

    Extention(String extension, String type, String description) {
        this.extension = extension;
        this.type = type;
        this.description = description;
    }

    public static String of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.type, inputType))
                .map(Extention::getType)
                .findAny()
                .orElseGet(none());
    }

    private static boolean euqalTo(String headerType, String inputType) {
        return headerType.equals(inputType);
    }

    public String getType() {
        return type;
    }

    private static Supplier<String> none() {
        return NONE::getType;
    }
}
