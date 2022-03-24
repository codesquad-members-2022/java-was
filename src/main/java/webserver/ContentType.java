package webserver;

import java.util.Objects;
import java.util.stream.Stream;

public enum ContentType {

    HTML("html", "text/html"),
    CSS("css", "text/css"),
    JS("js", "application/javascript"),
    OTHER("*", "*/*");

    private String extension;
    private String mime;

    ContentType(String extension, String mime) {
        this.extension = extension;
        this.mime = mime;
    }

    public static ContentType from(String ext) {
        return Stream.of(values())
            .filter(contentType -> contentType.equalsExt(ext))
            .findAny()
            .orElse(OTHER);
    }

    private boolean equalsExt(String extension) {
        return Objects.equals(this.extension, extension);
    }

    public String getMime() {
        return mime;
    }
}
