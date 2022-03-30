package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

import util.HttpRequestUtils;
import util.IOUtils;

public class Request {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private final Map<String, String> headers;
    private final String headerLine;
    private final String method;
    private final String url;
    private String body;

    public Request(String headerLine, Map<String, String> headers) {
        this.headerLine = headerLine;
        this.headers = headers;
        this.method = headerLine.split(" ")[0];
        this.url = headerLine.split(" ")[1];
    }

    public void generateBody(BufferedReader bufferedReader) {
        int contentLength;
        try {
            contentLength = getContentLength();
            String bodyData = IOUtils.readData(bufferedReader, contentLength);
            this.body = HttpRequestUtils.urlDecoding(bodyData);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public int getContentLength() {
        return Integer.parseInt(headers.get("Content-Length"));
    }

    public String getBody() {
        return body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
