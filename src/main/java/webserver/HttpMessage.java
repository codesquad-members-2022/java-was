package webserver;

import static util.HttpRequestUtils.parseQueryString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.IOUtils;

public class HttpMessage {
	private static final Logger log = LoggerFactory.getLogger(HttpMessage.class);
	private List<String> messages;
	private Map<String, String> header;

	private RequestHeader requestHeader;

	public HttpMessage() {
		messages = new ArrayList<>();
		header = new HashMap<>();
	}

	public void write(BufferedReader bufferedReader) {
		try {
			String line = bufferedReader.readLine();
			messages.add(line);
			log.debug(line);
			while (!line.equals("")) {
				line = bufferedReader.readLine();
				messages.add(line);
			}
			this.requestHeader = new RequestHeader(messages);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public boolean getMapping() {
		return requestHeader.isGetMethod();
	}

	public boolean postMapping() {
		return requestHeader.isPostMethod();
	}

	public String getPath() {
		return requestHeader.getPath();
	}

	public Map<String, String> getQueryString() {
		String queryParams = requestHeader.getQueryParams();
		return parseQueryString(queryParams);
	}

	public Map<String, String> getBody(BufferedReader bufferedReader) {
		try {
			String body = IOUtils.readData(bufferedReader, requestHeader.contentLength());
			return parseQueryString(requestHeader.toDecode(body));
		} catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
	}
}
