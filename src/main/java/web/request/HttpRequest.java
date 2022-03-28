package web.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;

    private Map<String, String> parameters;
    private int contentLength;
    private String body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        validateRequestExistence(line);
        log.debug("requestLine = {}", line);
        this.requestLine = new RequestLine(line);

        while (!"".equals(line)) {
            line = br.readLine();
            log.debug("requestHeader = {}", line);
            if (line.startsWith("Content-Length")) {
                this.contentLength = Integer.parseInt(line.substring(line.indexOf(':') + 2));
            }
        }
        this.body = initBody(br);
        this.parameters = initParameters();
    }

    private void validateRequestExistence(String line) {
        if (line == null) {
            throw new IllegalStateException("요청이 유효하지 않습니다.");
        }
    }

    private String initBody(BufferedReader br) throws IOException {
        String body = "";
        if (contentLength > 0) {
            body = IOUtils.readData(br, contentLength);
        }
        return HttpRequestUtils.decodeUrl(body);
    }

    private Map<String, String> initParameters() {
        //method가 Get일 경우
        HttpMethod method = requestLine.getMethod();
        if (method == HttpMethod.GET) {
            return requestLine.getQueryParameters();
        }
        //method가 Post인 경우
        return HttpRequestUtils.parseQueryString(body);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public HttpMethod getMethod() {
        return requestLine.getMethod();
    }

    public String getProtocol() {
        return requestLine.getProtocol();
    }
}
