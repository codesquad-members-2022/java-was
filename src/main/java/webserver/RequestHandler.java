package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import webserver.controller.DefaultController;
import webserver.controller.UserCreateController;

public class RequestHandler extends Thread {

	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
			connection.getPort());

		try (InputStream in = connection.getInputStream();
			OutputStream out = connection.getOutputStream();
			BufferedReader br = new BufferedReader(
				new InputStreamReader(in, StandardCharsets.UTF_8));
			DataOutputStream dos = new DataOutputStream(out)) {

			Request request = createRequest(br);
			Response response = new Response();

			// /user/create Controller
			if ("/user/create".equals(request.getUri())) {
				UserCreateController userController = new UserCreateController(request, response);
				userController.service();
			} else { // 그 외 Controller
				DefaultController defaultController = new DefaultController(request, response);
				defaultController.service();
			}

			sendResponse(dos, response);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private Request createRequest(BufferedReader br) throws IOException {
		List<String> rawHeader = new ArrayList<>();

		// read Request Header
		String line = "";
		int contentLength = 0;
		while (!"".equals(line = br.readLine())) {
			rawHeader.add(line);

			if (line.contains("Content-Length")) {
				String[] data = line.split(": ");
				contentLength = Integer.parseInt(data[1]);
			}
		}

		// read Request Body
		String rawBody = "";
		if (contentLength > 0) {
			rawBody = IOUtils.readData(br, contentLength);
		}

		return new Request(rawHeader, rawBody);
	}

	private void sendResponse(DataOutputStream dos, Response response) {
		try {
			String header = response.toHeader();
			byte[] body = response.toBody().getBytes(StandardCharsets.UTF_8);
			dos.writeBytes(header);
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
