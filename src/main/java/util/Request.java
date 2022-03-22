package util;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class Request {

	private static final int PATH = 0;
	private static final int QUERY_STRING = 1;

	private String[] parsedRequestLine;

	public Request(String requestLine) {
		this.parsedRequestLine = requestLine.split(" ");
	}

	public String takePath() {
		return parseRequestURL()[PATH];
	}

	public String takeQueryString() {
		if (parseRequestURL().length > 1) {
			return URLDecoder.decode(parseRequestURL()[QUERY_STRING], StandardCharsets.UTF_8);
		}
		return null;
	}

	private String[] parseRequestURL() {
		return parsedRequestLine[1].split("\\?");
	}

}
