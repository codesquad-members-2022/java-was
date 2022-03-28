package webserver;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequest {

    private static final String QUERY_SEPARATOR = "?";
    private static final String QUERY_SEPARATOR_REGEX = "\\" + QUERY_SEPARATOR;
    private static final String CONTENT_LENGTH = "Content-Length";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String COOKIE = "Cookie";
    private static final String APPLICATION_X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    private static final String REQUEST_LINE_DELIMITER = " ";
    private static final String QUERY_DELIMITER = "&";
    private static final String COOKIE_DELIMITER = ";";

    private static final String COOKIE_KEY_VALUE_SEPARATOR = "=";
    private static final String QUERY_KEY_VALUE_SEPARATOR = "=";
    private static final String HEADER_KEY_VALUE_SEPARATOR = ": ";
    private static final String LINE_SEPARATOR = System.lineSeparator();


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

        httpRequest.method = getMethod(requestLine);
        httpRequest.uri = getURI(requestLine);
        httpRequest.path = getPath(httpRequest.getUri());
        httpRequest.headers = parseHeaders(readHeaders(br));

        if (httpRequest.uri.contains(QUERY_SEPARATOR)) {
            httpRequest.parameters = parseQueryString(httpRequest.uri.split(QUERY_SEPARATOR_REGEX)[1]);
        }

        String requestBody = "";

        if (httpRequest.getHeader(CONTENT_LENGTH) != null) {
            int contentLength = Integer.parseInt(httpRequest.getHeader(CONTENT_LENGTH));
            requestBody = URLDecoder.decode(readData(br, contentLength), StandardCharsets.UTF_8);
        }

        if (APPLICATION_X_WWW_FORM_URLENCODED.equals(httpRequest.getHeader(CONTENT_TYPE))) {
            httpRequest.parameters = parseQueryString(requestBody);
        }

        String cookies = httpRequest.getHeader(COOKIE);

        if (cookies != null) {
            httpRequest.cookies = parseCookies(cookies);
        }

        return httpRequest;
    }

    public boolean isLoggedIn() {
        return Boolean.parseBoolean(cookies.getOrDefault("logged_in", "false"));
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

    public String getPath() {
        return this.path;
    }

    public Map<String, String> getParameters() {
        if (this.parameters == null) {
            return Map.of();
        }

        return Map.copyOf(this.parameters);
    }

    public Map<String, String> getCookies() {
        if (this.cookies == null) {
            return Map.of();
        }

        return Map.copyOf(this.cookies);
    }

    private static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    private static String getMethod(String requestLine) {
        return requestLine.split(REQUEST_LINE_DELIMITER)[0];
    }

    private static String getURI(String requestLine) {
        return requestLine.split(REQUEST_LINE_DELIMITER)[1];
    }

    private static String getPath(String requestURI) {
        return requestURI.split(QUERY_SEPARATOR_REGEX)[0];
    }

    private static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, COOKIE_DELIMITER, COOKIE_KEY_VALUE_SEPARATOR);
    }

    private static String readHeaders(BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;

        while (!(line = br.readLine()).isEmpty()) {
            sb.append(line).append(LINE_SEPARATOR);
        }

        return sb.toString();
    }

    private static Map<String, String> parseHeaders(String headers) {
        return parseValues(headers, LINE_SEPARATOR, HEADER_KEY_VALUE_SEPARATOR);
    }

    private static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, QUERY_DELIMITER, QUERY_KEY_VALUE_SEPARATOR);
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
