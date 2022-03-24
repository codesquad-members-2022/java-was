package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
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

	private final BufferedReader br;

	private String requestLine;
	private String[] parsedRequestLine;
	private String httpMethod;
	private String path;
	private List<Pair> headerPairs;
	private Map<String, String> parsedQueryString;
	private String requestBody;

	public Request(InputStream in) {
		InputStreamReader inputReader = new InputStreamReader(in);
		br = new BufferedReader(inputReader);
	}

	public void readRequest() throws IOException {
		extractRequestLine(br);
		this.headerPairs = IOUtils.readRequestHeader(br);
		this.requestBody = URLDecoder
			.decode(IOUtils.readData(br, takeContentLength()), StandardCharsets.UTF_8);
	}

	private void extractRequestLine(BufferedReader br) throws IOException {
		this.requestLine = br.readLine();
		this.parsedRequestLine = requestLine.split(" ");
		this.httpMethod = parsedRequestLine[HTTP_METHOD];
		this.path = parseRequestURL()[PATH];
		this.parsedQueryString = takeParsedQueryString();
	}

	private int takeContentLength() {
		//todo 빈 거 넣지 말고 null 방법은?
		return Integer.parseInt(
			headerPairs.stream()
				.filter(Pair::isContentLength)
				.findAny()
				.orElseGet(() -> new Pair("Content-Length", "0"))
				.getValue());
	}

	private String[] parseRequestURL() {
		return parsedRequestLine[REQUEST_TARGET].split("\\?");
	}

	private Map<String, String> takeParsedQueryString() {
		String queryString = takeQueryString();
		return HttpRequestUtils.parseQueryString(queryString);
	}

	public Map<String, String> takeParsedBody() {
		return HttpRequestUtils.parseQueryString(requestBody);
	}

	private String takeQueryString() {
		if (parseRequestURL().length > 1) {
			return URLDecoder.decode(parseRequestURL()[QUERY_STRING], StandardCharsets.UTF_8);
		}
		return null;
	}

	public List<Pair> getHeaderPairs() {
		return headerPairs;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getParsedQueryString() {
		return parsedQueryString;
	}

	public String getRequestLine() {
		return requestLine;
	}
}
