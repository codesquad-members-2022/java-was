package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

public class Request {

    private HttpMethod httpMethod;
    private Map<String, String> parameters = new HashMap<>();

    public static Request of(HttpRequestData requestData) {
        HttpRequestLine httpRequestLine = requestData.getHttpRequestLine();
        Request request = new Request();
        String queryString = httpRequestLine.getQueryString();
        HttpMethod httpMethod = httpRequestLine.getHttpMethod();
        request.setHttpMethod(httpMethod);

        if (isGetRequest(queryString, httpMethod)) {
            request.setParameters(HttpRequestUtils.parseQueryString(queryString));
            return request;
        }

        request.setParameters(requestData.getRequestBody());
        return request;
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
