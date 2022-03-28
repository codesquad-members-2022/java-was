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

    public static HttpRequest parse(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String[] requestLine = reader.readLine().split(" ");
        Map<String, String> header = parseHttpHeader(reader);
        return buildRequest(reader, header, requestLine);
    }

    private static Map<String, String> parseHttpHeader(BufferedReader reader) throws IOException {
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

    private static HttpRequest buildRequest(BufferedReader reader, Map<String, String> header, String[] requestLineTokens) throws IOException {
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
