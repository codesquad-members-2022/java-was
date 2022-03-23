package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private String method;
    private String url;
    private Map<String, String> requestHeader;
    private String requestBody;

    public Request(BufferedReader br) {
        try {
            makeRequestLine(br);
            makeRequestHeader(br);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeRequestLine(BufferedReader br) throws IOException {
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        this.method = HttpRequestUtils.getMethod(line);
        this.url = HttpRequestUtils.getUrl(line);
    }

    private void makeRequestHeader(BufferedReader br) throws IOException {
        this.requestHeader = HttpRequestUtils.readRequestHeader(br);
    }

    public void makeRequestBody(BufferedReader br) throws IOException {
        int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
        this.requestBody = URLDecoder.decode(IOUtils.readData(br, contentLength),
            StandardCharsets.UTF_8);
    }

    public String getRequestBody() {
        return requestBody;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

}
