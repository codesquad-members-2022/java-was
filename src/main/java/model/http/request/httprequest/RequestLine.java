package model.http.request.httprequest;

import model.http.HttpMethod;
import model.http.HttpVersion;

public class RequestLine {

    private static final int METHOD_TYPE_INDEX = 0;
    private static final int URL_INDEX = 1;
    private static final int VERSION_INDEX = 2;

    private final HttpMethod httpMethod;
    private final String url;
    private final HttpVersion httpVersion;

    public RequestLine(String[] startLine) {
        this.httpMethod = getHttpMethod(startLine);
        this.url = getUrl(startLine);
        this.httpVersion = getHttpVersion(startLine);
    }

    private HttpMethod getHttpMethod(String[] startLine) {
        String methodType = startLine[METHOD_TYPE_INDEX];
        return HttpMethod.getMethod(methodType);
    }

    private String getUrl(String[] startLine) {
        return startLine[URL_INDEX];
    }

    private HttpVersion getHttpVersion(String[] startLine) {
        return HttpVersion.of(startLine[VERSION_INDEX]);
    }

    public String getHttpRequestUrl() {
        return this.url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
