package webserver;

import java.util.Objects;
import java.util.stream.Stream;

public enum AcceptType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("application/javascript", "text/html"),
    UNKNOWN("*", "*/*");

    private String ext;
    private String contentType;

    AcceptType(String ext, String contentType) {
        this.ext = ext;
        this.contentType = contentType;
    }

    public static AcceptType from(String ext) {
        return Stream.of(values())
            .filter(acceptType -> acceptType.equalsExt(ext))
            .findAny()
            .orElse(UNKNOWN);
    }

    private boolean equalsExt(String ext) {
        return Objects.equals(this.ext, ext);
    }
}
