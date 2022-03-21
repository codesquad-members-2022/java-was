package webserver;

import java.util.Objects;
import java.util.stream.Stream;

public enum ContentType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    UNKNOWN("*", "*/*");

    private String ext;
    private String mime;

    ContentType(String ext, String mime) {
        this.ext = ext;
        this.mime = mime;
    }

    public static ContentType from(String ext) {
        return Stream.of(values())
            .filter(contentType -> contentType.equalsExt(ext))
            .findAny()
            .orElse(UNKNOWN);
    }

    private boolean equalsExt(String ext) {
        return Objects.equals(this.ext, ext);
    }

    public String getMime() {
        return mime;
    }
}
