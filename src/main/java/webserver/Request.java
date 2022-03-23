package webserver;

import java.util.Map;

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

    public String parseExt() {
        String[] tokens = url.split("\\.");
        return tokens[tokens.length - 1];
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
