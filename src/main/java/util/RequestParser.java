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

        RequestLineTokens token = tokenMaker(reader.readLine());
        Map<String, String> header = parseHttpHeader(reader);
        return buildRequest(reader, header, token);
    }

    private static RequestLineTokens tokenMaker(String requestLine) {
        String[] splits = requestLine.split(" ");
        return new RequestLineTokens(splits[0], splits[1], splits[2]);
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

    private static HttpRequest buildRequest(BufferedReader reader, Map<String, String> header, RequestLineTokens token) throws IOException {
        String body = "";

        if (token.method.equals("POST")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            body = IOUtils.readData(reader, contentLength);
        }

        return new HttpRequest.Builder().method(token.method)
                .path(token.path)
                .protocol(token.protocol)
                .header(header)
                .body(body)
                .build();
    }

    private static class RequestLineTokens {
        private String method;
        private String path;
        private String protocol;

        public RequestLineTokens(String method, String path, String protocol) {
            this.method = method;
            this.path = path;
            this.protocol = protocol;
        }
    }
}
