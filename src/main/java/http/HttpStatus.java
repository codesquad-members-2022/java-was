package http;

public enum HttpStatus {
    OK(200),
    BAD_REQUEST(400),
    INTERNAL_SERVER_ERROR(500);

    private int statusCode;

    HttpStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
}
