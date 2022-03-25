package web.request;

import util.HttpRequestUtils;

import java.util.Map;

public class RequestLine {
    private HttpMethod method;
    private String path;
    private Map<String, String> queryParameters;
    private String protocol;

    public RequestLine(String requestLineString) {
        parseRequestLine(requestLineString);
    }

    private void parseRequestLine(String requestLineString) {
        String[] tokens = requestLineString.split(" ");
        this.method = HttpMethod.valueOf(tokens[0]);
        initPathAndQueryParameters(tokens[1]);
        this.protocol = tokens[2];
    }

    private void initPathAndQueryParameters(String url) {
        String decodedUrl = HttpRequestUtils.decodeUrl(url);
        String path;
        String queryString = "";
        if (decodedUrl.contains("?")) {
            int queryStringStartIndex = decodedUrl.indexOf('?');
            queryString = decodedUrl.substring(queryStringStartIndex + 1);
            path = decodedUrl.substring(0, queryStringStartIndex);
        } else {
            path = decodedUrl;
        }
        this.path = path;
        this.queryParameters = HttpRequestUtils.parseQueryString(queryString);
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryParameters() {
        return queryParameters;
    }

    public String getProtocol() {
        return protocol;
    }
}
