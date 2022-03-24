package http;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private List<String> headerMessages = new ArrayList<>();
    private String path;
    private String method;
    private Map<String, String> paramMap;

    public HttpRequest(BufferedReader bf) throws IOException {
        String line = bf.readLine();
        while (!(line.equals("") || line == null)) {
            headerMessages.add(URLDecoder.decode(line, "UTF-8"));
            line = bf.readLine();
        }

        String firstLine = headerMessages.get(0);
        this.path = HttpRequestUtils.parsePath(firstLine);
        this.method = HttpRequestUtils.parseMethod(firstLine);
        this.paramMap = HttpRequestUtils.parseQueryString(firstLine);
    }

    public List<String> getHeaderMessages() {
        return headerMessages;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }
}
