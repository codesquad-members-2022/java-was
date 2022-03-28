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
    private final Map<String, String> body;

    public Request(String line, Map<String, String> headers, Map<String, String> body) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];

        path = parsePath();
        queryString = parseQueryString();
        contentType = toContentType();

        this.headers = headers;
        this.body = body;
    }

    public Request(String line, Map<String, String> header) {
        this(line, header, null);
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

    public String getQueryStringValue(String parameter) {
        return queryString.get(parameter);
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getHeaderValue(String key) {
        return headers.get(key);
    }

    public String getBodyValue(String key) {
        return body.get(key);
    }

}
