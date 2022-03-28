package com.riakoader.was.httpmessage;

import com.google.common.base.Strings;
import com.riakoader.was.util.HttpRequestUtils;

import java.util.Map;

public class HttpRequest {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String QUERYSTRING_DELIMITER = "?";

    private final String method;
    private final String requestURI;
    private final String protocol;
    private Map<String, String> params;
    private final Map<String, String> headers;

    public HttpRequest(String requestLine, Map<String, String> headers, String requestMessageBody) {
        String[] requestLineTokens = requestLine.split(REQUEST_LINE_DELIMITER);
        String requestURL = requestLineTokens[1];
        int queryStringDelimiterIndex = requestURL.indexOf(QUERYSTRING_DELIMITER);

        this.method = requestLineTokens[0];
        queryStringDelimiterIndex = queryStringDelimiterIndex != -1 ? queryStringDelimiterIndex : requestURL.length();
        this.requestURI = requestURL.substring(0, queryStringDelimiterIndex);

        this.protocol = requestLineTokens[2];

        if (queryStringDelimiterIndex != requestURL.length()) {
            this.params = HttpRequestUtils.parseQueryString(requestURL.substring(queryStringDelimiterIndex + 1));
        }

        this.headers = headers;

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
