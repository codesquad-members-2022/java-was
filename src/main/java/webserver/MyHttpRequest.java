package webserver;

import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyHttpRequest {
    private String method;
    private String requestURL;
    private String requestURI;
    private String protocol;
    private Map<String, String> paramMap;

    public MyHttpRequest(InputStream in) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        try {
            initStartLine(bufferedReader);
            // TODO : initHeader();
            // TODO : initBody();
        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private void initStartLine(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        String[] tokens = line.split(" ");
        method = tokens[0];
        requestURL = tokens[1];
        int ampersandIdx = tokens[1].length()-1;
        for (int i = 0; i < tokens[1].length(); i++) {
            if (tokens[1].charAt(i) == '&') {
                ampersandIdx = i;
                break;
            }
        }
        requestURI = tokens[1].substring(0, ampersandIdx);
        paramMap = HttpRequestUtils.parseQueryString(tokens[1].substring(ampersandIdx+1));
        protocol = tokens[2];
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
}
