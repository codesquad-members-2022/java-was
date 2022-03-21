package com.riakoader.was.httpmessage;


import com.riakoader.was.util.HttpRequestUtils;

import java.util.List;

public class HttpClientRequest {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private String method;
    private String url;
    private List<HttpRequestUtils.Pair> headers;

    public HttpClientRequest(String requestLine, List<HttpRequestUtils.Pair> headers) {
        this.method = requestLine.split(REQUEST_LINE_DELIMITER)[0];
        this.url = requestLine.split(REQUEST_LINE_DELIMITER)[1];
        this.headers = headers;
    }

    public String getUrl() {
        return url;
    }
}
