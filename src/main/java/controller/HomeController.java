package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import webserver.Request;
import webserver.Response;

public class HomeController implements Controller {

	private static final HomeController instance = new HomeController();

	private HomeController() {
	}

	public static HomeController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
		response.response200Header(body);
		response.responseBody(body);
	}

}
