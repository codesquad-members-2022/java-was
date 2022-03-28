package webserver;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private String httpMethod;
    private String httpUrl;
    private String httpVersion;
    private Map<String, String> params;
    private List<Pair> headers;

    public HttpRequest(String requestLine, List<Pair> headers) throws IOException {
        String[] requestLineSplit = requestLine.split(" ");
        this.httpMethod = requestLineSplit[0];
        this.httpUrl = requestLineSplit[1];
        this.httpVersion = requestLineSplit[2];
        this.params = parseParams(httpUrl);
        this.headers = headers;
    }

    private Map<String, String> parseParams(String httpUrl) {
        String[] splitUrl = httpUrl.split("\\?");
        if (hasQueryString(splitUrl)) {
            return HttpRequestUtils.parseQueryString(splitUrl[1]);
        }

        return new HashMap<>();
    }

    private boolean hasQueryString(String[] params) {
        return params.length > 1;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getParameter(String key) {
        return params.get(key);
    }

    public void setParameters(Map<String, String> params) {
        this.params = params;
    }

}
