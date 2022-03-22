package model;

import java.util.Arrays;
import java.util.function.Supplier;

public enum HeaderType {

    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    NONE("");

    private final String value;

    HeaderType(String value) {
        this.value = value;
    }

    public static String of(String inputType) {
        return Arrays.stream(values())
                .filter(headerType -> euqalTo(headerType.value, inputType))
                .map(HeaderType::getValue)
                .findAny()
                .orElseGet(none());
    }

    private static boolean euqalTo(String headerType, String inputType) {
        return headerType.equals(inputType);
    }

    public String getValue() {
        return value;
    }

    private static Supplier<String> none() {
        return NONE::getValue;
    }
}
