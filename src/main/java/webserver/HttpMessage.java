package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpMessage {
	private static final Logger log = LoggerFactory.getLogger(HttpMessage.class);
	private List<String> messages;

	public HttpMessage() {
		messages = new ArrayList<>();
	}

	public void write(InputStream inputStream) {
		try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
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
		return this.messages.get(0);
	}
}
