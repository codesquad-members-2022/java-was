package webserver;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class RequestLine {

    private String httpMethod;
    private String path;
    private String httpVersion;

    public RequestLine(String httpMethod, String path, String httpVersion) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.httpVersion = httpVersion;
    }

    public String getPath() {
        return path;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public boolean isPost() {
        return "POST".equals(httpMethod);
    }

    public Map<String, String> parseParameter() {
        String[] splitPath = path.split("\\?");
        if (splitPath.length <= 1) {
            return new HashMap<>();
        }
        return HttpRequestUtils.parseQueryString(splitPath[1]);
    }
}
