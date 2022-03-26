package webserver;

import java.util.Map;
import util.HttpRequestUtils;

public class Request {

    private final String method;
    private final String url;
    private final String protocol;

    private final String path;
    private final Map<String, String> queryString;
    private final ContentType contentType;

    private final Map<String, String> headers;

    public Request(String line, Map<String, String> headers) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];

        path = parsePath();
        queryString = parseQueryString();
        contentType = toContentType();

        this.headers = headers;
    }

    public String parsePath() {
        return url.split("\\?")[0];
    }

    public Map<String, String> parseQueryString() {
        String[] tokens = url.split("\\?");
        return HttpRequestUtils.parseQueryString(tokens.length < 2 ? null : tokens[1]);
    }

    public ContentType toContentType() {
        String[] tokens = url.split("\\.");
        String ext = tokens[tokens.length - 1];
        return ContentType.from(ext);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
