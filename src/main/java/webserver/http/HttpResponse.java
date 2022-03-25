package webserver.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private final String version;
    private final int httpStatusCode;
    private final String httpStatusMessage;
    private final Map<String, String> responseHeaders = new HashMap<>();
    private byte[] responseBody = new byte[]{};

    public HttpResponse(String version, int httpStatusCode, String httpStatusMessage) {
        this.version = version;
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    public String getVersion() {
        return version;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public String getHeader(String key) {
        return responseHeaders.get(key);
    }

    public byte[] getResponseBody() {
        return responseBody;
    }

    public void addHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void addBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }
}
