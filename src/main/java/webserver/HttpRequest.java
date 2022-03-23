package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String httpMethod;
    private String absPath;
    private Map<String, String> header;
    private Map<String, String> params;

    public HttpRequest(BufferedReader br) throws IOException {
        String[] requestLine = IOUtils.readRequestLine(br).split(" ");

        httpMethod = requestLine[0];
        absPath = requestLine[1];
        header = IOUtils.readRequestHeader(br);
        if (httpMethod.equals("POST")) {
            params = parseBody(IOUtils.readData(br, Integer.parseInt(header.get("Content-Length"))));
        }else{
            params = new HashMap<>();
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getAbsPath() {
        return absPath;
    }

    public Map<String, String> getHeader() {
        return Collections.unmodifiableMap(header);
    }

    public Map<String, String> getParams() {
        return Collections.unmodifiableMap(params);
    }

    private Map<String, String> parseBody(String body) {
        return HttpRequestUtils.parseQueryString(body);
    }
}
