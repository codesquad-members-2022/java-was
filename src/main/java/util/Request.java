package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

public class Request {

	private Logger log = LoggerFactory.getLogger(Request.class);

	private static final int HTTP_METHOD = 0;
	private static final int REQUEST_TARGET = 1;
	private static final int PATH = 0;
	private static final int QUERY_STRING = 1;

	private List<Pair> headerPairs;
	private String[] parsedRequestLine;

	public Request(String requestLine) {
		this.parsedRequestLine = requestLine.split(" ");
	}

	public String takeHttpMethod() {
		return parsedRequestLine[HTTP_METHOD];
	}

	public String takePath() {
		return parseRequestURL()[PATH];
	}

	public int takeContentLength() {
		return Integer.parseInt(
			headerPairs.stream()
			.filter(Pair::isContentLength)
			.findAny()
			.orElseThrow(() -> new IllegalStateException("Content-Length가 존재하지 않음"))
			.getValue());
	}

	private String[] parseRequestURL() {
		return parsedRequestLine[REQUEST_TARGET].split("\\?");
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
