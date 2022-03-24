package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import util.HttpRequestUtils.Pair;

public class Request {

	private static final int PATH = 0;
	private static final int QUERY_STRING = 1;
	private List<Pair> headerPairs;

	private String[] parsedRequestLine;

	public Request(String requestLine) {
		this.parsedRequestLine = requestLine.split(" ");
	}

	public String takePath() {
		return parseRequestURL()[PATH];
	}

	private String[] parseRequestURL() {
		return parsedRequestLine[1].split("\\?");
	}

	public List<Pair> takeHeaderPairs(BufferedReader br) throws IOException {
		this.headerPairs = IOUtils.readRequestHeader(br);
		return new ArrayList<>(headerPairs);
	}

	public Map<String, String> takeParsedQueryString() {
		String queryString = takeQueryString();
		return HttpRequestUtils.parseQueryString(queryString);
	}

	private String takeQueryString() {
		if (parseRequestURL().length > 1) {
			return URLDecoder.decode(parseRequestURL()[QUERY_STRING], StandardCharsets.UTF_8);
		}
		return null;
	}
}
