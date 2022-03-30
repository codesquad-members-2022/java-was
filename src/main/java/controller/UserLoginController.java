package controller;

import db.DataBase;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

		if (user != null && user.getPassword().equals(parsedBody.get("password"))) {
			response.newResponse302("http://localhost:8080/index.html", user.getUserId());
			return;
		}
		response.newResponse302("http://localhost:8080/user/login_failed.html");
	}

}
