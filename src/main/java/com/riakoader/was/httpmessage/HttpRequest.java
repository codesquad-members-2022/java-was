package com.riakoader.was.httpmessage;

import com.google.common.base.Strings;
import com.riakoader.was.util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String QUERYSTRING_DELIMITER = "?";

    private String method;
    private String requestURI;
    private String protocol;
    private Map<String, String> params;
    private Map<String, String> headers;

    public HttpRequest(String requestLine, Map<String, String> headers, String requestMessageBody) {
        parseRequestLine(requestLine);
        setHeaders(headers);
        parseRequestMessageBody(requestMessageBody);
    }

    private void parseRequestLine(String requestLine) {
        String[] requestLineTokens = requestLine.split(REQUEST_LINE_DELIMITER);
        this.method = requestLineTokens[0];
        splitQuery(requestLineTokens[1]);
        this.protocol = requestLineTokens[2];
    }

    private void splitQuery(String requestURI) {
        int queryStringDelimiterIndex = requestURI.indexOf(QUERYSTRING_DELIMITER);
        queryStringDelimiterIndex = queryStringDelimiterIndex != -1 ? queryStringDelimiterIndex : requestURI.length();
        this.requestURI = requestURI.substring(0, queryStringDelimiterIndex);

        if (queryStringDelimiterIndex != requestURI.length()) {
            this.params = HttpRequestUtils.parseQueryString(requestURI.substring(queryStringDelimiterIndex + 1));
        }
    }

    private void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    private void parseRequestMessageBody(String requestMessageBody) {
        if (!Strings.isNullOrEmpty(requestMessageBody)) {
            this.params = HttpRequestUtils.parseQueryString(requestMessageBody);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getParameter(String name) {
        return params.get(name);
    }
}
