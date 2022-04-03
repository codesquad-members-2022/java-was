package web.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.Pair;
import web.common.Cookie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class HttpRequest {

    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private Map<String, String> parameters;
    private Map<String, Cookie> cookies;
    private Map<String, String> headers;
    private String body;

    public HttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        this.requestLine = initRequestLine(br);
        this.headers = initHeaders(br);
        this.body = initBody(br);
        this.parameters = initParameters();
        this.cookies = initCookies();
    }

    private RequestLine initRequestLine(BufferedReader br) throws IOException {
        String requestLineString = br.readLine();
        validateRequestExistence(requestLineString);
        return new RequestLine(requestLineString);
    }

    private Map<String, String> initHeaders(BufferedReader br) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line = br.readLine();
        while (!line.isEmpty()) {
            log.debug("requestHeader = {}", line);
            String key = line.substring(0, line.indexOf(":"));
            String value = line.substring(line.indexOf(':') + 2);
            headers.put(key, value);
            line = br.readLine();
        }
        return headers;
    }

    private void validateRequestExistence(String line) {
        if (line == null) {
            throw new IllegalStateException("요청이 유효하지 않습니다.");
        }
    }

    private String initBody(BufferedReader br) throws IOException {
        String body = "";
        int contentLength = getContentLength();
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

    private Map<String, Cookie> initCookies() {
        return HttpRequestUtils.parseCookies(getHeader("Cookie"))
                .entrySet()
                .stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toMap(Pair::getKey, Cookie::new));
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

    public int getContentLength() {
        return Optional.ofNullable(headers.get("Content-Length"))
                .map(Integer::parseInt)
                .orElse(0);
    }

    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public Cookie getCookie(String cookieName) {
        return cookies.get(cookieName);
    }
}
