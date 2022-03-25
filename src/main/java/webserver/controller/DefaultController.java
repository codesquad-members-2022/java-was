package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DefaultController extends Controller {

	private static final DefaultController instance = new DefaultController();

	private DefaultController() {
		super();
	}

	public static DefaultController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		byte[] body = null;
		body = Files.readAllBytes(new File("./webapp/" + request.getUri()).toPath());
		response.setBody(body, "text/html");
	}
}
