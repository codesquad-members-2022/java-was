package webserver.dto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import http.HttpMethod;

public class RequestLine {

    private static final int REQUEST_HTTP_METHOD_INDEX = 0;
    private static final int REQUEST_URI_INDEX = 1;
    private static final int URI_INDEX = 0;
    private static final int QUERYSTRING_INDEX = 1;

    private HttpMethod httpMethod;
    private String url;
    private String queryString;

    public static RequestLine of(String firstLine) {
        String[] firstLineSplit = firstLine.split(" ");
        String[] result = firstLineSplit[REQUEST_URI_INDEX].split("\\?");

        HttpMethod httpMethod = HttpMethod.findMethod(firstLineSplit[REQUEST_HTTP_METHOD_INDEX]).orElseThrow();
        RequestLine requestLine = new RequestLine(httpMethod, result[URI_INDEX]);

        if (result.length > 1) {
            requestLine.setQueryString(URLDecoder.decode(result[QUERYSTRING_INDEX], StandardCharsets.UTF_8));
        }

        return requestLine;
    }

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
