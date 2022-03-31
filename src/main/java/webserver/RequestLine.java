package webserver;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class RequestLine {

    private String httpMethod;
    private String path;
    private String httpVersion;

    public RequestLine(String requestLine) {
        String[] requestLineSplit = requestLine.split(" ");
        this.httpMethod = requestLineSplit[0];
        this.path = requestLineSplit[1];
        this.httpVersion = requestLineSplit[2];
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
