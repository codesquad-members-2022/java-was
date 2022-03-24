package web.request;

import util.HttpRequestUtils;

import java.util.Map;

public class MyRequestLine {
    private MyHttpMethod method;
    private String url;
    private String path;
    private String queryString;
    private Map<String, String> queryParameters;

    public MyRequestLine(String requestLineString) {
        parseRequestLine(requestLineString);
        initPathAndQueryString();
        initQueryParameters();
    }

    private void parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        this.method = MyHttpMethod.valueOf(tokens[0]);
        this.url = tokens[1];
    }

    private void initPathAndQueryString() {
        String path = "";
        String queryString = "";
        if (url.contains("?")) {
            int queryStringStartIndex = url.indexOf('?');
            queryString = url.substring(queryStringStartIndex + 1);
            path = url.substring(0, queryStringStartIndex);
        } else {
            path = url;
        }
        this.path = HttpRequestUtils.decodeUrl(path);
        this.queryString = HttpRequestUtils.decodeUrl(queryString);
    }

    private void initQueryParameters() {
        this.queryParameters = HttpRequestUtils.parseQueryString(queryString);
    }

    public MyHttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }
}
