package webserver;

public enum HttpResponseStatusCode {
    OK("HTTP/1.1 200 OK\r\n"),
    NOT_FOUND("HTTP/1.1 404 Not Found \r\n");

    private final String value;
    HttpResponseStatusCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
