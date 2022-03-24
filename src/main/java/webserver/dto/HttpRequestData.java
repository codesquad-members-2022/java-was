package webserver.dto;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestData {

    private HttpRequestLine httpRequestLine;
    private Map<String, String> header = new HashMap<>();
    private Map<String, String> requestBody = new HashMap<>();

    public HttpRequestLine getHttpRequestLine() {
        return httpRequestLine;
    }

    public void setHttpRequestLine(HttpRequestLine httpRequestLine) {
        this.httpRequestLine = httpRequestLine;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public Map<String, String> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, String> requestBody) {
        this.requestBody = requestBody;
    }
}
