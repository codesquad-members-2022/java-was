package http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import util.HttpRequestUtils;
import webserver.dto.RequestLine;

public class Request {

    private Map<String, String> queryParameter = new HashMap<>();
    private HttpMethod httpMethod;

    public static Request of(RequestLine requestLine) {
        Request request = new Request();
        String queryString = requestLine.getQueryString();
        if (queryString != null && !queryString.equals("")) {
            request.setQueryParameter(HttpRequestUtils.parseQueryString(queryString));
        }
        request.setHttpMethod(requestLine.getHttpMethod());
        return request;
    }

    public Map<String, String> getQueryParameter() {
        return Collections.unmodifiableMap(queryParameter);
    }

    public void setQueryParameter(Map<String, String> queryParameter) {
        this.queryParameter = queryParameter;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
