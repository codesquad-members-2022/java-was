package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class MyHttpRequest {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private String method;
    private String requestURL;
    private String requestURI;
    private String protocol;
    private Map<String, String> paramMap = new HashMap<>();

    private Map<String, String[]> headersMap;

    public MyHttpRequest(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        initStartLine(bufferedReader);
        initHeaders(bufferedReader);
        if (method.equals("POST")) {
            initBodyQueryString(bufferedReader);
        }
        // TODO : initBody();
    }

    private void initBodyQueryString(BufferedReader bufferedReader) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Integer.parseInt(getHeader("Content-Length")[0]); ++i) {
            sb.append((char)bufferedReader.read());
        }
        String line = new String(sb);
        Map<String, String> bodyParamMap = HttpRequestUtils.parseQueryString(line);
        paramMap.putAll(bodyParamMap);
    }

    private void initStartLine(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        String[] tokens = line.split(" ");
        method = tokens[0];
        requestURL = tokens[1];
        int questionMarkIdx = tokens[1].indexOf('?');
        if (questionMarkIdx != -1) {
            requestURI = tokens[1].substring(0, questionMarkIdx);
            if (questionMarkIdx != tokens[1].length()) {
                paramMap = HttpRequestUtils.parseQueryString(tokens[1].substring(questionMarkIdx + 1));
            }
        } else {
            requestURI = tokens[1];
        }
        protocol = tokens[2];
    }

    private void initHeaders(BufferedReader bufferedReader) throws IOException {
        headersMap = new HashMap<>();
        String line = "";
        while (((line = bufferedReader.readLine()) != null) && !line.equals("")) {
            String[] tokens = line.split(": ");
            String headerName = tokens[0];
            String[] headerValues = tokens[1].split(",");

            headersMap.put(headerName, headerValues);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public String[] getHeader(String name) {
        return headersMap.get(name);
    }
}
