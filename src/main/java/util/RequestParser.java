package util;

import http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {
    private static final Logger log = LoggerFactory.getLogger(RequestParser.class);

    private final BufferedReader reader;

    public RequestParser(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    public HttpRequest createRequest() throws IOException {
        String[] requestLine = reader.readLine().split(" ");
        Map<String, String> header = parseHttpHeader();
        return buildRequest(header, requestLine);
    }

    private Map<String, String> parseHttpHeader() throws IOException {
        Map<String, String> header = new HashMap<>();
        String line = reader.readLine();
        while (!"".equals(line)) {
            String[] headerTokens = line.split(": ");
            if (headerTokens.length == 2) {
                header.put(headerTokens[0], headerTokens[1]);
                log.debug("[HEADER] : {}, {}", headerTokens[0], headerTokens[1]);
            }
            line = reader.readLine();
        }
        return header;
    }

    private HttpRequest buildRequest(Map<String, String> header, String[] requestLineTokens) throws IOException {
        String body = "";

        if (requestLineTokens[0].toLowerCase().equals("post")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            body = IOUtils.readData(reader, contentLength);
        }

        return new HttpRequest.Builder().method(requestLineTokens[0])
                .path(requestLineTokens[1])
                .protocol(requestLineTokens[2])
                .header(header)
                .body(body)
                .build();
    }
}
