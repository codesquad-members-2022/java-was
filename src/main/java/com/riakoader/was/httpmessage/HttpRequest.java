package com.riakoader.was.httpmessage;

import com.google.common.base.Strings;
import com.riakoader.was.session.HttpSession;
import com.riakoader.was.util.HttpRequestUtils;

import java.util.Collections;
import java.util.Map;

public class HttpRequest {

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String QUERYSTRING_DELIMITER = "?";

    private String method;
    private String requestURI;
    private String protocol;
    private Parameters params;
    private Headers headers;
    private Map<String, String> cookies;
    private final HttpSession session = HttpSession.getInstance();

    public HttpRequest(String requestLine, Map<String, String> headers, String requestMessageBody) {
        parseRequestLine(requestLine);
        setHeadersAndCookies(headers);
        parseRequestMessageBody(requestMessageBody);
    }

    private void parseRequestLine(String requestLine) {
        String[] requestLineTokens = requestLine.split(REQUEST_LINE_DELIMITER);
        this.method = requestLineTokens[0];
        extractQuery(requestLineTokens[1]);
        this.protocol = requestLineTokens[2];
    }

    private void extractQuery(String requestURI) {
        int queryStringDelimiterIndex = requestURI.indexOf(QUERYSTRING_DELIMITER);
        queryStringDelimiterIndex = queryStringDelimiterIndex != -1 ? queryStringDelimiterIndex : requestURI.length();
        this.requestURI = requestURI.substring(0, queryStringDelimiterIndex);

        if (queryStringDelimiterIndex != requestURI.length()) {
            this.params = new Parameters(
                    HttpRequestUtils.parseQueryString(requestURI.substring(queryStringDelimiterIndex + 1))
            );
        }
    }

    private void setHeadersAndCookies(Map<String, String> headers) {
        this.headers = new Headers(headers);
        cookies = Strings.isNullOrEmpty(headers.get("Cookie")) ? Collections.emptyMap() : HttpRequestUtils.parseCookies(headers.get("Cookie"));
    }

    private void parseRequestMessageBody(String requestMessageBody) {
        if (!Strings.isNullOrEmpty(requestMessageBody)) {
            this.params = new Parameters(HttpRequestUtils.parseQueryString(requestMessageBody));
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
        return params.getValue(name);
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public HttpSession getSession() {
        return session;
    }
}
