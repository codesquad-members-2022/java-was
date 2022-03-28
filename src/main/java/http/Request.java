package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

public class Request {

    private static final String COOKIE = "Cookie";
    public static final String SESSION_ID = "sessionId";

    private HttpMethod httpMethod;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    private String sessionId;

    /**
     * TODO : Cookie를 객체로 변경
     *
     */

    public static Request of(HttpRequestData requestData) {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();
        String queryString = httpRequestLine.getQueryString();
        HttpMethod httpMethod = httpRequestLine.getHttpMethod();

        Request request = new Request();
        request.setHttpMethod(httpMethod);

        Map<String, String> header = requestData.getHeader();
        if (header.containsKey(COOKIE)) {
            String cookie = header.get(COOKIE);
            request.cookies = HttpRequestUtils.parseCookies(cookie);
            if (request.cookies.containsKey(SESSION_ID)) {
                request.sessionId = request.cookies.get(SESSION_ID);
            }
        }

        if (isGetRequest(queryString, httpMethod)) {
            request.setParameters(HttpRequestUtils.parseQueryString(queryString));
            return request;
        }

        request.setParameters(requestData.getRequestBody());
        return request;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getCookies(String cookieName) {
        return cookies.get(cookieName);
    }

    private static boolean isGetRequest(String queryString, HttpMethod httpMethod) {
        return queryString != null && !queryString.equals("") && httpMethod.equals(HttpMethod.GET);
    }

    public Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
