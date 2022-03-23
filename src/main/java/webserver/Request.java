package webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Request {

    private final static String QUERY_FLAG = "\\?";
    private final String method;
    private final String uri;
    private final String version;
    private final String body;
    private final Map<String, String> queryStringMap;
    private final Map<String, String> requestHeaderMap = new HashMap<>();

    public Request(String requestLine, List<String> rawHeader, String rawBody) {
        String[] tokens = requestLine.split(" ");
        method = tokens[0];
        uri = parseUri(tokens[1]);
        version = tokens[2];
        parseRequestHeaderMap(rawHeader);
        if (requestHeaderMap.getOrDefault("Content-Type", "").equals("application/x-www-form-urlencoded")) {
            queryStringMap = HttpRequestUtils.parseQueryString(rawBody);
            body = null;
        } else {
            queryStringMap = parseQueryStringMap(tokens[1]);
            body = rawBody;
        }
    }

    private String parseUri(String rawUri) {
        if (isQueryString(rawUri)) {
            String[] uri_tokens = rawUri.split(QUERY_FLAG);
            return uri_tokens[0];
        }
        return rawUri;
    }

    private Map<String, String> parseQueryStringMap(String rawUri) {
        if (isQueryString(rawUri)) {
            String[] uri_tokens = rawUri.split(QUERY_FLAG);
            return HttpRequestUtils.parseQueryString(uri_tokens[1]);
        }
        return new HashMap<>();
    }

    private boolean isQueryString(String uri) {
        return uri.contains("?");
    }

    private void parseRequestHeaderMap(List<String> rawData) {
        for (String rawDatum : rawData) {
            if (!rawDatum.isEmpty()) {
                Pair pair = HttpRequestUtils.parseHeader(rawDatum);
                requestHeaderMap.put(pair.getKey(), pair.getValue());
            }
        }
    }

    public Map<String, String> getRequestHeaderMap() {
        return requestHeaderMap;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getQueryStringMap() {
        return queryStringMap;
    }

    public String getParam(String keyOfparamMap) {
        return queryStringMap.get(keyOfparamMap);
    }
}
