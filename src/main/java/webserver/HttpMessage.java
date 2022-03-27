package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpMessage {
	private static final Logger log = LoggerFactory.getLogger(HttpMessage.class);
	public static final String SEPARATOR_OF_QUERY_STRINGS = "?";
	public static final String SEPARATOR_OF_SPACE = "\\p{Blank}";
	private List<String> messages;
	private Map<String, String> header;

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

				// POST
				if (line.indexOf(":") > 0) {
					String[] splitHeader = line.split(":");
					String key = splitHeader[0].trim();
					String value = splitHeader[1].trim();
					log.debug("header - {}:{}",key,value);
					header.put(key, value);
				}

			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	// POST
	public int contentLength() {
		return Integer.parseInt(this.header.get("Content-Length"));
	}

	// GET
	public String readFirstOfHeader() {
		return this.messages.get(0).split(SEPARATOR_OF_SPACE)[1];
	}

	public String getQueryParams() {
		String path = this.readFirstOfHeader();
		String params = path.substring(path.indexOf(SEPARATOR_OF_QUERY_STRINGS) + 1);
		return toDecode(params);
	}

	public String toDecode(String url) {
		return URLDecoder.decode(url, StandardCharsets.UTF_8);
	}
}
