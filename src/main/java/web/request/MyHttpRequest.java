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

public class MyHttpRequest {

    private final Logger log = LoggerFactory.getLogger(MyHttpRequest.class);

    private String requestLine;
    private String method;
    private String url;
    private String path;
    private String queryString;
    private Map<String, String> parameters;
    private int contentLength;
    private String body;

    public MyHttpRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = br.readLine();
        if (line == null) {
            throw new IllegalStateException("요청이 유효하지 않습니다.");
        }
        log.debug("requestLine = {}", line);
        this.requestLine = line;
        parseRequestLine(requestLine);

        while (!"".equals(line)) {
            line = br.readLine();
            log.debug("requestHeader = {}", line);
            if (line.startsWith("Content-Length")) {
                this.contentLength = Integer.parseInt(line.substring(line.indexOf(':') + 2));
            }
        }
        this.body = initBody(br);
        initPathAndQueryString();
        this.parameters = initParameters();
    }

    private Map<String, String> initParameters() {
        //method가 Get일 경우
        if ("GET".equals(method)) {
            return HttpRequestUtils.parseQueryString(queryString);
        }
        //method가 Post인 경우
        return HttpRequestUtils.parseQueryString(body);
    }

    private void parseRequestLine(String requestLine) {
        String[] tokens = requestLine.split(" ");
        this.method = tokens[0];
        this.url = tokens[1];
    }

    private String initBody(BufferedReader br) throws IOException {
        String body = "";
        if (contentLength > 0) {
            body = IOUtils.readData(br, contentLength);
        }
        return body;
    }

    private void initPathAndQueryString() {
        String path = "";
        String queryString = "";
        if (url.contains("?")) {
            int queryStringStartIndex = url.indexOf('?');
            queryString = url.substring(queryStringStartIndex + 1);
            path = url.substring(0, queryStringStartIndex);
        } else {
            path = url;
        }
        this.path = path;
        this.queryString = queryString;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getPath() {
        return this.path;
    }
}
