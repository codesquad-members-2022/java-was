package webserver;

import db.DataBase;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

	private Logger log = LoggerFactory.getLogger(Response.class);
	private byte[] body = new byte[0];
	private DataOutputStream dos;
	private Request request;

	public Response(OutputStream out, Request request) {
		this.dos = new DataOutputStream(out);
		this.request = request;
	}

	public void writeResponse() throws IOException {
		log.debug("requestLine: {}", request.getRequestLine());
		if (HttpMethod.isGet(request) && request.getPath().equals("/user/logout")) {
			logoutHeader("http://localhost:8080/index.html");
			responseBody();
			return;
		}

		if (HttpMethod.isPost(request) && request.getPath().equals("/user/create")) {

			Map<String, String> parsedBody = request.takeParsedBody();
			log.debug("POST BODY: {}", parsedBody);
			User user = new User(
				parsedBody.get("userId"),
				parsedBody.get("password"),
				parsedBody.get("name"),
				parsedBody.get("email")
			);
			saveUser(user);
			return;
		}

		if (HttpMethod.isPost(request) && request.getPath().equals("/user/login")) {
			Map<String, String> parsedBody = request.takeParsedBody();
			log.debug("POST BODY: {}", parsedBody);

			User user = DataBase.findUserById(parsedBody.get("userId"));

			if (user != null && user.getPassword().equals(parsedBody.get("password"))) {
				response302Header("http://localhost:8080/index.html", user.getUserId());
				responseBody();
				return;
			}
			response302Header("http://localhost:8080/user/login_failed.html");
			responseBody();
			return;
		}

		//GET 일 때
		this.body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
		response200Header();
		responseBody();
	}

	private void saveUser(User user) {
		if (DataBase.validateDuplicatedId(user)) {
			DataBase.addUser(user);
			response302Header("http://localhost:8080/index.html");
			responseBody();
			return;
		}
		response302Header("http://localhost:8080/user/form.html");
		responseBody();
	}

	private void response302Header(String redirectURL) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("Location: " + redirectURL + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(String redirectURL, String userId) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("Location: " + redirectURL + "\r\n");
			dos.writeBytes("Set-Cookie: sessionId=" + userId + "; max-age=20; Path=/; HttpOnly\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void logoutHeader(String redirectURL) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("Location: " + redirectURL + "\r\n");
			dos.writeBytes("Set-Cookie: sessionId=; max-age=-1; Path=/\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void response200Header(byte[] body) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void responseBody(byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void response200Header() {
		try {
			dos.writeBytes("HTTP/1.1 200 OK\r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + body.length + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void responseBody() {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}
