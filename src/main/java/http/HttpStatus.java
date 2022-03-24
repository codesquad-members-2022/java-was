package http;

public enum HttpStatus {
    OK(200),
    SEE_OTHER(303),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500);

    private final int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
