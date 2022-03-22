package util;

public class Request {

	private static final int PATH = 0;
	private static final int QUERY_STRING = 1;

	private String requestLine;
	private String[] parsedRequestLine;

	public Request(String requestLine) {
		this.requestLine = requestLine;
		this.parsedRequestLine = requestLine.split(" ");
	}

	public String takePath() {
		return parseRequestURL()[PATH];
	}

	public String takeQueryString() {

		if (parseRequestURL().length > 1) {
			return parseRequestURL()[QUERY_STRING];
		}
		return null;
	}

	private String[] parseRequestURL() {
		return parsedRequestLine[1].split("\\?");
	}

}
