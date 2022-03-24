package webserver;

import java.util.Map;

public class Request {

    private final String httpMethod;
    private final String url;
    private final String queryString;
    private final Map<String, String> headers;
    private final String requestBody;

    public Request(String httpMethod, String url, String queryString, Map<String, String> headers, String requestBody) {
        this.httpMethod = httpMethod;
        this.url = url;
        this.queryString = queryString;
        this.headers = headers;
        this.requestBody = requestBody;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public boolean isSameMethod(String method) {
        return this.httpMethod.equals(method);
    }

}
