package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import util.HttpRequestUtils;
import util.IOUtils;
import util.Pair;

public class Request {

	private static final int HTTP_METHOD = 0;
	private static final int REQUEST_TARGET = 1;
	private static final int PATH = 0;
	private static final int QUERY_STRING = 1;

	private final BufferedReader br;
	private String requestLine;
	private String[] parsedRequestLine;
	private HttpMethod httpMethod;
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
		this.requestBody = decode(IOUtils.readData(br, takeContentLength()));
	}

	private void extractRequestLine(BufferedReader br) throws IOException {
		this.requestLine = br.readLine();
		this.parsedRequestLine = requestLine.split(" ");
		this.httpMethod = HttpMethod.create(parsedRequestLine[HTTP_METHOD]);
		this.path = parseRequestURL()[PATH];
		this.parsedQueryString = takeParsedQueryString();
	}

	private int takeContentLength() {
		Optional<Pair> pair = headerPairs.stream()
			.filter(Pair::isContentLength)
			.findAny();
		return pair.map(p -> Integer.parseInt(p.getValue())).orElse(0);
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
		return parseRequestURL().length > 1 ? decode(parseRequestURL()[QUERY_STRING]) : null;
	}

	private String decode(String target) {
		return URLDecoder.decode(target, StandardCharsets.UTF_8);
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public List<Pair> getHeaderPairs() {
		return headerPairs;
	}

	public String getPath() {
		return path;
	}

	public String getRequestLine() {
		return requestLine;
	}
}
