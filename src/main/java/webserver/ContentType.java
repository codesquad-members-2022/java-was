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

    public static String findType(String url) {
        if(url.endsWith("js")) {
            return JS.type;
        }
        if(url.endsWith("css")) {
            return CSS.type;
        }
        return HTML.type;
    }
}
