package controller;

import java.util.ArrayList;
import java.util.List;
import util.Pair;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class UserLogoutController implements Controller {

	private static final UserLogoutController instance = new UserLogoutController();

	private UserLogoutController() {
	}

	public static UserLogoutController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) {
		List<Pair> pairs = new ArrayList<>();
		pairs.add(new Pair("Location", "http://localhost:8080/index.html"));
		pairs.add(new Pair("Set-Cookie", "sessionId=; max-age=-1; Path=/"));
		response.write(HttpStatus.FOUND, pairs);
	}

}
