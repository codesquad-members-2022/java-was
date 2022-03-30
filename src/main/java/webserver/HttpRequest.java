package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String httpMethod;
    private String path;
    private Map<String, String> header;
    private Map<String, String> params;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String[] requestLine = br.readLine().split(" ");

        httpMethod = requestLine[0];
        path = requestLine[1];
        header = IOUtils.readRequestHeader(br);
        if (httpMethod.equals("POST")) {
            params = parseBody(IOUtils.readData(br, Integer.parseInt(header.get("Content-Length"))));
        } else {
            params = new HashMap<>();
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
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
