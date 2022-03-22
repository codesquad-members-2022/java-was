package webserver.dto;

import http.HttpMethod;

public class RequestLine {
    private HttpMethod httpMethod;
    private String url;
    private String queryString;

    public RequestLine(HttpMethod httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
