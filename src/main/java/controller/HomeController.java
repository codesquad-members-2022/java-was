package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class HomeController implements Controller {

	private static final HomeController instance = new HomeController();

	private Logger log = LoggerFactory.getLogger(HomeController.class);
	private HomeController() {
	}

	public static HomeController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
		log.debug("path: {}", request.getPath());
		response.write(body, HttpStatus.OK);
	}
}
