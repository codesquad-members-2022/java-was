package webserver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String method;
    private String url;
    private String protocol;

    private Map<String, String> header = new HashMap<>();

    public Request(String line, Map<String, String> header) throws IOException {
        parseLine(line);
        this.header = header;
    }

    private void parseLine(String line) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];
    }

    public String parseExt() {
        String[] tokens = url.split("\\.");
        return tokens[tokens.length - 1];
    }

    public String getUrl() {
        return url;
    }
}
