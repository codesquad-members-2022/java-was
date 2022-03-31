package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

public class RequestHandler extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private static final FrontController frontController = FrontController.getInstance();

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream();
			BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(in, StandardCharsets.UTF_8));
			DataOutputStream dataOutputStream = new DataOutputStream(out)) {

			Request request = createRequest(bufferedReader);
			Response response = new Response();

			frontController.service(request, response);

			sendResponse(dataOutputStream, response);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private Request createRequest(BufferedReader bufferedReader) throws IOException {
		List<String> rawMessageHeader = new ArrayList<>();

		// read Request Header
		String line = "";
		int contentLength = 0;
		while (!"".equals(line = bufferedReader.readLine())) {
			rawMessageHeader.add(line);

			if (line.contains("Content-Length")) {
				String[] data = line.split(": ");
				contentLength = Integer.parseInt(data[1]);
			}
		}

		// read Request Body
		String rawBody = "";
		if (contentLength > 0) {
			rawBody = IOUtils.readData(bufferedReader, contentLength);
		}

		return new Request(rawMessageHeader, rawBody);
	}

	private void sendResponse(DataOutputStream dataOutputStream, Response response) {
		try {
			String header = response.toHeader();
			byte[] body = response.toBody().getBytes(StandardCharsets.UTF_8);
			dataOutputStream.writeBytes(header);
			dataOutputStream.write(body, 0, body.length);
			dataOutputStream.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
