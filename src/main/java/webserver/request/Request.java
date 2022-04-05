package webserver.request;

import util.HttpRequestUtils;

import java.util.Map;

public class Request {

    private String protocol;
    private String httpMethod;
    private String url;
    private String queryString;
    private final Map<String, String> headers;
    private final String requestBody;

    public Request(String requestLine, Map<String, String> headers, String requestBody) {
        parseRequestLine(requestLine);
        this.headers = headers;
        this.requestBody = requestBody;
    }

    private void parseRequestLine(String requestLine) {
        String[] splited = HttpRequestUtils.splitRequestLine(requestLine);
        this.httpMethod = splited[0];
        this.url = splited[1];
        if (splited.length == 3) {
            this.protocol = splited[2];
        }
        if(splited.length == 4) {
            this.queryString = splited[2];
            this.protocol = splited[3];
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public String getHttpMethod() {
        return httpMethod;
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

    public String getSessionId() {
        String cookie = headers.get("Cookie");
        return cookie.split("=")[1];
    }

}
