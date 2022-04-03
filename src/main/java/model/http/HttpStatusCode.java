package model.http;

public enum HttpStatusCode {

    OK(200, Series.SUCCESSFUL, "OK"),

    MOVED_PERMANENTLY(301, Series.REDIRECTION, "Moved Permanently"),
    FOUND(302, Series.REDIRECTION, "Found"),

    BAD_REQUEST(400, Series.CLIENT_ERROR, "Bad Request"),

    INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR, "Internal Server Error");

    HttpStatusCode(int code, Series series, String reason) {
        this.code = code;
        this.series = series;
        this.reason = reason;
    }

    private final int code;
    private final Series series;
    private final String reason;

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return code + " " + reason;
    }
}
