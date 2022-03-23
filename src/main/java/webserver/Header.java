package webserver;

import com.google.common.base.Strings;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Header {

    private static final String CONTENT_LENGTH = "Content-Length";

    private final String httpMethod;
    private final String url;
    private final String queryString;
    private final Map<String, String> headers;

    public Header(BufferedReader bufferedReader) throws IOException {
        String requestLine = bufferedReader.readLine();
        this.httpMethod = HttpRequestUtils.getMethod(requestLine);
        this.url = HttpRequestUtils.parseUrl(requestLine);
        this.queryString = HttpRequestUtils.getQueryString(requestLine);
        this.headers = saveHeader(bufferedReader);
    }

    private Map<String, String> saveHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> headerMap = new HashMap<>();
        String headerLine = bufferedReader.readLine();
        while(!Strings.isNullOrEmpty(headerLine)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headerMap.put(pair.getKey(), pair.getValue());
            headerLine = bufferedReader.readLine();
        }
        return headerMap;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryString() {
        return queryString;
    }

    public int getContentLength() {
        return Integer.parseInt(headers.get(CONTENT_LENGTH));
    }

    public boolean isSameMethod(String method) {
        return this.httpMethod.equals(method);
    }
}
