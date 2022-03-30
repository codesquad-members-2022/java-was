package webserver;

public enum ContentType {
    HTML("text/html;"),
    CSS("text/css;"),
    JS("application/javascript;");

    private final String type;

    ContentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ContentType from(String url) {
        if(url.endsWith("js")) {
            return JS;
        }
        if(url.endsWith("css")) {
            return CSS;
        }
        return HTML;
    }
}
