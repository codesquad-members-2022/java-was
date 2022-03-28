package webserver.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final String method;
    private final String url;
    private final String version;
    private final Map<String, String> requestHeader;
    private String body;
    private Map<String, String> params = new HashMap<>();
    private final boolean isLogin;

    public HttpRequest(String method, String url, String version, Map<String, String> requestHeader, String body, boolean isLogin) {
        this.method = method;
        if (url.contains("?")) {
            this.url = url.split("\\?")[0];
            String queryString = HttpRequestUtils.parseUrl(url);
            params = HttpRequestUtils.parseQueryString(queryString);
        } else {
            this.url = url;
        }
        this.version = version;
        this.requestHeader = requestHeader;
        this.body = body;
        params.putAll(HttpRequestUtils.parseQueryString(body));
        this.isLogin = isLogin;

        log.debug("params: {}", params);
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    public String getBody() {
        return body;
    }

    public String getParameter(String parameterName) {
        return params.get(parameterName);
    }
}
