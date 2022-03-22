package webserver;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequest {

    private String method;
    private String uri;
    private String path;
    private Map<String, String> headers;
    private Map<String, String> parameters;
    private Map<String, String> cookies;

    private HttpRequest() {

    }

    public static HttpRequest receive(BufferedReader br) throws IOException {
        HttpRequest httpRequest = new HttpRequest();
        String requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

        httpRequest.method = getRequestMethod(requestLine);
        httpRequest.uri = getRequestURI(requestLine);
        httpRequest.path = getRequestPath(httpRequest.getUri());
        httpRequest.headers = parseHeaders(readHeaders(br));

        if (httpRequest.uri.contains("?")) {
            httpRequest.parameters = parseQueryString(httpRequest.uri.split("\\?")[1]);
        }

        String requestBody = "";

        if (httpRequest.getHeader("Content-Length") != null) {
            int contentLength = Integer.parseInt(httpRequest.getHeader("Content-Length"));
            requestBody = URLDecoder.decode(readData(br, contentLength), StandardCharsets.UTF_8);
        }

        if ("application/x-www-form-urlencoded".equals(httpRequest.getHeader("Content-Type"))) {
            httpRequest.parameters = parseQueryString(requestBody);
        }

        String cookies;

        if ((cookies = httpRequest.getHeader("Cookie")) != null) {
            httpRequest.cookies = parseCookies(cookies);
        }

        return httpRequest;
    }

    public String getUri() {
        return uri;
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public boolean hasMethodEqualTo(String method) {
        return this.method.equals(method);
    }

    public boolean hasPathEqualTo(String path) {
        return this.path.equals(path);
    }

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getParameters() {
        if (this.parameters == null) {
            return null;
        }

        return new HashMap<>(this.parameters);
    }

    public Map<String, String> getCookies() {
        if (this.cookies == null) {
            return null;
        }

        return Map.copyOf(this.cookies);
    }

    private static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    private static String getRequestMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    private static String getRequestURI(String requestLine) {
        return requestLine.split(" ")[1];
    }

    private static String getRequestPath(String requestURI) {
        return requestURI.split("\\?")[0];
    }

    private static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";", "=");
    }

    private static String readHeaders(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while (!(line = br.readLine()).isEmpty()) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private static Map<String, String> parseHeaders(String headers) {
        return parseValues(headers, "\n", ": ");
    }

    private static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&", "=");
    }

    private static Map<String, String> parseValues(String values, String separator, String keyValueSeparator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
            .map(t -> getKeyValue(t, keyValueSeparator))
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    private static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    private static class Pair {
        private final String key;
        private final String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + key.hashCode();
            result = prime * result + value.hashCode();

            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            Pair other = (Pair) obj;

            return (key.equals(other.key) && value.equals(other.value));
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
