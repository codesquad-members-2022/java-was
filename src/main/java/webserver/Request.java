package webserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

public class Request {

	private final String method;
	private final String uri;
	private final String version;
	private final Map<String, String> paramMap;
	private final Map<String, String> requestHeaderMap = new HashMap<>();


	public Request(String firstLine, List<String> rawData) {
		// method, uri, paramQuery, version
		String[] tokens = firstLine.split(" ");
		method = tokens[0];
		if (tokens[1].contains("?")) {
			String[] uri_tokens = tokens[1].split("\\?");
			paramMap = HttpRequestUtils.parseQueryString(uri_tokens[1]);
			uri = uri_tokens[0];
		} else {
			uri = tokens[1];
			paramMap = null;
		}
		version = tokens[2];

		// header
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

	public Map<String, String> getParamMap() {
		return paramMap;
	}

	public String getParam(String keyOfparamMap){
		return paramMap.get(keyOfparamMap);
	}
}
