package webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Request {

	private final static String QUERY_FLAG = "\\?";
	private final String method;
	private final String uri;
	private final String fileExtension;
	private final String version;
	private final String body;
	private final Map<String, String> queryStringMap;
	private final Map<String, String> requestHeaderMap;
	private final Map<String, String> cookieMap;

	public Request(List<String> rawMessageHeader, String rawBody) {
		String requestLine = rawMessageHeader.get(0);
		List<String> rawRequestHeaders = rawMessageHeader.subList(1, rawMessageHeader.size());

		method = getRequestLineOf(requestLine, "method");
		uri = getRequestLineOf(requestLine, "uri");
		version = getRequestLineOf(requestLine, "version");
		fileExtension = parseFileExtention(uri);
		requestHeaderMap = parseRequestHeaderMap(rawRequestHeaders);
		queryStringMap = parseQueryStringMap(rawBody, requestLine);
		cookieMap = parseCookieMap(requestHeaderMap.get("Cookie"));
		body = parseBody(rawBody);
	}

	private String getRequestLineOf(String requestLine, String type) {
		String[] tokens = requestLine.split(" ");
		switch (type) {
			case "method":
				return tokens[0];
			case "uri":
				return tokens[1];
			case "version":
				return tokens[2];
			default:
				return "";
		}
	}

	private String parseBody(String rawBody) {
		if (requestHeaderMap.getOrDefault("Content-Type", "")
			.equals("application/x-www-form-urlencoded")) {
			return null;
		} else {
			return rawBody;
		}
	}

	private Map<String, String> parseQueryStringMap(String rawBody, String requestLine) {
		String[] tokens = requestLine.split(" ");
		String rawUri = tokens[1];

		if (requestHeaderMap.getOrDefault("Content-Type", "")
			.equals("application/x-www-form-urlencoded")) {
			return HttpRequestUtils.parseQueryString(rawBody);
		} else {
			return parseUriQueryStringMap(rawUri);
		}
	}

	private Map<String, String> parseCookieMap(String cookie) {
		return HttpRequestUtils.parseCookies(cookie);
	}

	private String parseFileExtention(String uri) {
		String fileExtention = uri.substring(uri.lastIndexOf(".") + 1);
		if (fileExtention.equals(uri)) {
			return "";
		}
		return fileExtention;
	}

	private Map<String, String> parseUriQueryStringMap(String rawUri) {
		if (isQueryString(rawUri)) {
			String[] uri_tokens = rawUri.split(QUERY_FLAG);
			return HttpRequestUtils.parseQueryString(uri_tokens[1]);
		}
		return new HashMap<>();
	}

	private boolean isQueryString(String uri) {
		return uri.contains("?");
	}

	private Map<String, String> parseRequestHeaderMap(List<String> rawData) {
		Map<String, String> requestHeaderMap = new HashMap<>();
		for (String rawDatum : rawData) {
			if (!rawDatum.isEmpty()) {
				Pair pair = HttpRequestUtils.parseHeader(rawDatum);
				requestHeaderMap.put(pair.getKey(), pair.getValue());
			}
		}
		return requestHeaderMap;
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

	public String getHeaderValue(String keyofHeader) {
		return requestHeaderMap.get(keyofHeader);
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public Optional<String> getCookieValue(String cookieName) {
		return Optional.ofNullable(cookieMap.get(cookieName));
	}
}
