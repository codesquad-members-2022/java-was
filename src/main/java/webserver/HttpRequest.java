package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static util.HttpRequestUtils.parseQueryString;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private List<String> messages;

    private RequestHeader requestHeader;

    public HttpRequest() {
        messages = new ArrayList<>();
    }

    public void write(BufferedReader bufferedReader) {
        try {
            String line = bufferedReader.readLine();
            messages.add(line);
            log.debug(line);
            while (!line.equals("")) {
                line = bufferedReader.readLine();
                messages.add(line);
            }
            this.requestHeader = new RequestHeader(messages);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public boolean getMapping() {
        return requestHeader.isGetMethod();
    }

    public boolean postMapping() {
        return requestHeader.isPostMethod();
    }

    public String getPath() {
        return requestHeader.getPath();
    }

    public Map<String, String> getQueryString() {
        String queryParams = requestHeader.getQueryParams();
        return parseQueryString(toDecode(queryParams));
    }

    public Map<String, String> getBody(BufferedReader bufferedReader) {
        try {
            String body = IOUtils.readData(bufferedReader, requestHeader.contentLength());
            return parseQueryString(toDecode(body));
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public String httpVersion() {
        return this.requestHeader.getVersion();
    }

    private String toDecode(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }
}
