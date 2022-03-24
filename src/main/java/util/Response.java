package util;

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
	private byte[] body;
	private DataOutputStream dos;
	private Request request;

	public Response(OutputStream out, Request request) {
		this.dos = new DataOutputStream(out);
		this.request = request;
	}

	public void writeResponse() throws IOException {
		if (request.getHttpMethod().equals("GET")) {

			log.debug("GET 요청, requestLine: {}", request.getRequestLine());

			this.body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
			response200Header();
			responseBody();
			return;
		}

		log.debug("POST 요청, requestLine: {}", request.getRequestLine());

		this.body = Files.readAllBytes(new File("./webapp/index.html").toPath());
		response302Header("http://localhost:8080/index.html");
		responseBody();

		Map<String, String> parsedBody = request.takeParsedBody();
		log.debug("POST BODY: {}", parsedBody);
		User user = new User(
			parsedBody.get("userId"),
			parsedBody.get("password"),
			parsedBody.get("name"),
			parsedBody.get("email")
		);

	}

	public void response302Header(String redirectURL) {
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

	public void response200Header() {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
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
