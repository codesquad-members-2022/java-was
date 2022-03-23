package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

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
			if ("/user/create".equals(request.getUri())) {
//				User user = new User(request.getParam("userId"),
//					request.getParam("password"),
//					request.getParam("name"),
//					request.getParam("email"));
//				log.debug("회원가입완료 {}",user );
				body = Files.readAllBytes(new File("./webapp/" + "index.html").toPath());
			} else {
				body = Files.readAllBytes(new File("./webapp/" + request.getUri()).toPath());
			}

			DataOutputStream dos = new DataOutputStream(out);
			response200Header(dos, body.length);
			responseBody(dos, body);

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private Request createRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
		List<String> rawHeader = new ArrayList<>();
		String line = br.readLine();
		String firstLine = line;

		//request header 읽기
		int contentLength = 0;
		while (!"".equals(line)) {
			line = br.readLine();
			rawHeader.add(line);
			//여기서 content-length만 파싱
			if(line.contains("Content-Length")){
				String[] data = line.split(": ");
				contentLength = Integer.valueOf(data[1]);
			}
			log.debug("rawHeader: {}" , line);
		}

		//조건이 있어야
		if(contentLength > 0) {
			String rawBody = IOUtils.readData(br, contentLength);
		}

		Request request = new Request(firstLine, rawHeader, rawBody);
		return request;
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

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
