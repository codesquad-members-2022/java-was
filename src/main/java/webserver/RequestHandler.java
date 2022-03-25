package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

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
			OutputStream out = connection.getOutputStream()) {
			Request request = createRequest(in);

			byte[] body = null;
			DataOutputStream dos = new DataOutputStream(out);
			if ("/user/create".equals(request.getUri())) {

				User findUser = DataBase.findUserById(request.getParam("userId"));
				if (findUser == null) { // 회원가입
					User user = new User(request.getParam("userId"),
							request.getParam("password"),
							request.getParam("name"),
							request.getParam("email"));
					DataBase.addUser(user);
					log.debug("회원가입완료 {}", user);
					response302Header(dos, "http://localhost:8080/index.html");
				} else { // 중복회원
					response302Header(dos, "http://localhost:8080/user/form.html");
				}

			} else {
				body = Files.readAllBytes(new File("./webapp/" + request.getUri()).toPath());
				response200Header(dos, body.length);
				responseBody(dos, body);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private Request createRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
		List<String> rawHeader = new ArrayList<>();
		String line = br.readLine();
		String requestLine = line;

		// read Request Header
		int contentLength = 0;
		while (!"".equals(line)) {
			line = br.readLine();
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

		return new Request(requestLine, rawHeader, rawBody);
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String redirectionUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + redirectionUrl + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
