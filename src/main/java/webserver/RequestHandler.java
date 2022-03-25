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
			OutputStream out = connection.getOutputStream();
			BufferedReader br = new BufferedReader(
				new InputStreamReader(in, StandardCharsets.UTF_8));
			DataOutputStream dos = new DataOutputStream(out)) {

			Request request = createRequest(br);
			Response response = new Response();

			// /user/create Controller
			if ("/user/create".equals(request.getUri())) {
				User findUser = DataBase.findUserById(request.getParam("userId"));
				if (findUser == null) { // 회원가입
					User user = new User(request.getParam("userId"),
						request.getParam("password"),
						request.getParam("name"),
						request.getParam("email"));
					DataBase.addUser(user);
					log.debug("회원가입완료 {}", user);
					response.setRedirect(StatusCode.REDIRECTION_302,
						"http://localhost:8080/index.html");
				} else { // 중복회원
					response.setRedirect(StatusCode.REDIRECTION_302,
						"http://localhost:8080/user/form.html");
				}
			} else { // 그 외 Controller
				byte[] body = null;
				body = Files.readAllBytes(new File("./webapp/" + request.getUri()).toPath());
				response.setBody(body, "text/html");
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
