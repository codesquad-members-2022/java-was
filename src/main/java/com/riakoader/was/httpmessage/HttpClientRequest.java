package com.riakoader.was.httpmessage;


import com.riakoader.was.util.HttpRequestUtils;

import java.util.List;

public class HttpClientRequest {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private String method;
    private String requestURI;
    private List<HttpRequestUtils.Pair> headers;

    public HttpClientRequest(String requestLine, List<HttpRequestUtils.Pair> headers) {
        String[] requestLineTokens = requestLine.split(REQUEST_LINE_DELIMITER);
        this.method = requestLineTokens[0];
        this.requestURI = requestLineTokens[1];
        this.headers = headers;
    }

    public String getRequestURI() {
        return requestURI;
    }
}
