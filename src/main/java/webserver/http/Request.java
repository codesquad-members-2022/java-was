package webserver.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.IOUtils;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private String requestLine;
    private String method;
    private String url;
    private Map<String, String> requestHeader;
    private String requestBody;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        makeRequestLine(br);
        makeRequestHeader(br);
        makeRequestBody(br);
    }

    private void makeRequestLine(BufferedReader br) throws IOException {
        this.requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        this.method = HttpRequestUtils.getMethod(requestLine);
        this.url = HttpRequestUtils.getUrl(requestLine);
    }

    private void makeRequestHeader(BufferedReader br) throws IOException {
        this.requestHeader = HttpRequestUtils.readRequestHeader(br);
    }

    private void makeRequestBody(BufferedReader br) throws IOException {
        if (requestHeader.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
            this.requestBody = URLDecoder.decode(IOUtils.readData(br, contentLength),
                StandardCharsets.UTF_8);
        }
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public String getUrl() {
        return url;
    }

    public Optional<String> getRequestBody() {
        return Optional.ofNullable(this.requestBody);
    }
}
