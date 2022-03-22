package web.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyHttpRequest {

    private final Logger log = LoggerFactory.getLogger(MyHttpRequest.class);

    private String requestLine;
    private String method;
    private String url;
    private String path;
    private Map<String, String> parameters;

    public MyHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException("요청이 유효하지 않습니다.");
        }
        log.debug("requestLine = {}", line);
        this.requestLine = line;
        this.url = HttpRequestUtils.parseUrl(requestLine);

        String queryString = "";
        if (url.contains("?")) {
            int queryStringStartIndex = url.indexOf('?');
            queryString = url.substring(queryStringStartIndex + 1);
            this.path = url.substring(0, queryStringStartIndex);
        } else {
            this.path = url;
        }

        this.parameters = HttpRequestUtils.parseQueryString(queryString);

        while(!"".equals(line)) {
            line = br.readLine();
            log.debug("requestHeader = {}", line);
        }
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getPath() {
        return this.path;
    }
}
