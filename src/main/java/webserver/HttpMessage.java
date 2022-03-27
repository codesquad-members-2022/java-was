package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpMessage {
	private static final Logger log = LoggerFactory.getLogger(HttpMessage.class);
	public static final String SEPARATOR_OF_QUERY_STRINGS = "?";
	public static final String SEPARATOR_OF_SPACE = "\\p{Blank}";
	private List<String> messages;

	public HttpMessage() {
		messages = new ArrayList<>();
	}

	public void write(BufferedReader bufferedReader) {
		try {
			String line = bufferedReader.readLine();
			log.debug(line);
			while (!line.equals("")) {
				messages.add(line);
				line = bufferedReader.readLine();
				log.debug(line);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public String readFirstOfHeader() {
		return this.messages.get(0).split(SEPARATOR_OF_SPACE)[1];
	}

	public String getQueryParams() {
		String path = this.readFirstOfHeader();
		String params = path.substring(path.indexOf(SEPARATOR_OF_QUERY_STRINGS) + 1);
		return toDecode(params);
	}

	private String toDecode(String url) {
		return URLDecoder.decode(url, StandardCharsets.UTF_8);
	}
}
