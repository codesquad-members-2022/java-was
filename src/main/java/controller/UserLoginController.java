package controller;

import db.DataBase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;
import webserver.HttpStatus;
import webserver.Request;
import webserver.Response;

public class UserLoginController implements Controller {

	private static final UserLoginController instance = new UserLoginController();

	private Logger log = LoggerFactory.getLogger(UserLoginController.class);

	private UserLoginController() {
	}

	public static UserLoginController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) {
		Map<String, String> parsedBody = request.takeParsedBody();
		log.debug("POST BODY: {}", parsedBody);

		User user = DataBase.findUserById(parsedBody.get("userId"));
		List<Pair> pairs = new ArrayList<>();

		if (user != null && user.getPassword().equals(parsedBody.get("password"))) {
			pairs.add(new Pair("Location", "http://localhost:8080/index.html"));
			pairs.add(new Pair("Set-Cookie", "sessionId=" + user.getUserId() + "; max-age=20; Path=/; HttpOnly"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}
		pairs.add(new Pair("Location", "http://localhost:8080/user/login_failed.html"));
		response.write(HttpStatus.FOUND, pairs);
	}


}
