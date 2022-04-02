package model.http;

import java.util.Arrays;
import java.util.function.Supplier;

import static util.SpecialCharacters.NEW_LINE;

public enum ContentType {

    HTML("html", "text/html;"),
    CSS("css", "text/css;"),
    JAVASCRIPT("js", "javascript"),
    ICO("ico", "image/x-icon;"),
    WOFF("wof", "application/font-woff;"),
    ALL("*", "*/*");

    private static final String CONTENT_TYPE = "Content-Type:";
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
        return ALL::getType;
    }

    @Override
    public String toString() {
        return CONTENT_TYPE + ContentType.of("") + NEW_LINE;
    }
}
