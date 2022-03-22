package webserver;

import java.util.Map;
import util.HttpRequestUtils;

public class Request {

    private final String method;
    private final String url;
    private final String protocol;

    private final Map<String, String> header;

    public Request(String line, Map<String, String> header) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];

        this.header = header;
    }

    public String parsePath() {
        return url.split("\\?")[0];
    }

    public Map<String, String> parseQueryString() {
        String[] tokens = url.split("\\?");
        return HttpRequestUtils.parseQueryString(tokens.length < 2 ? "" : tokens[1]);
    }

    public String toContentType() {
        String[] tokens = url.split("\\.");
        String ext = tokens[tokens.length - 1];
        return ContentType.from(ext).getMime();
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

    public Map<String, String> getHeader() {
        return header;
    }
}
