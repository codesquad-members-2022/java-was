package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private final int METHOD = 0;
    private final int URL = 1;
    private final int HTTP_VERSION = 2;
    private final int KEY = 0;
    private final int VALUE = 1;

    private String httpMethod;
    private String requestUrl;
    private String httpVersion;
    private Map<String, String> requestHeaderField = new HashMap<>();
    private Map<String, String> requestBody = new HashMap<>();

    public Request(InputStream in) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        setRequestLine(input);
        setRequestHeader(input);
        setRequestBody(input);
    }

    private void setRequestLine(BufferedReader input) throws IOException {
        String requestLine = URLDecoder.decode(input.readLine(), StandardCharsets.UTF_8);
        log.debug("<<<<<request start>>>>>");
        log.debug("[request line] : {}", requestLine);

        String[] requestInfo = HttpRequestUtils.getRequestInfo(requestLine);
        httpMethod = requestInfo[METHOD];
        requestUrl = requestInfo[URL];
        httpVersion = requestInfo[HTTP_VERSION];
    }

    private void setRequestHeader(BufferedReader input) throws IOException {
        String line;
        while (!"".equals(line = URLDecoder.decode(input.readLine(), StandardCharsets.UTF_8))) {
            if (line == null) {
                return;
            }
            Pair pair = HttpRequestUtils.parseHeader(line);
            requestHeaderField.put(pair.getKey(), pair.getValue());
        }

        requestHeaderField.entrySet().forEach(e -> {
            log.debug("{} : {}", e.getKey(), e.getValue());
        });
        log.debug("<<<<<request end>>>>>");
    }

    private void setRequestBody(BufferedReader input) throws IOException {
        int contentLength = (requestHeaderField.get("Content-Length") == null) ?
                0 : Integer.parseInt(requestHeaderField.get("Content-Length"));
        String data = IOUtils.readData(input, contentLength);
        String decodedData = URLDecoder.decode(data, StandardCharsets.UTF_8);
        requestBody = HttpRequestUtils.parseRequestBody(decodedData);
    }

    public Pair methodUrl() {
        return new Pair(httpMethod, requestUrl);
    }

    public String findBodyByFieldName(String fieldName) {
        return requestBody.get(fieldName);
    }
}
