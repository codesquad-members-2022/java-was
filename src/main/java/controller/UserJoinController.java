package controller;

import db.DataBase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;

public class UserJoinController implements Controller {

	private static final UserJoinController instance = new UserJoinController();

	private Logger log = LoggerFactory.getLogger(UserJoinController.class);

	private UserJoinController() {
	}

	public static UserJoinController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		Map<String, String> parsedBody = request.takeParsedBody();
		log.debug("POST BODY: {}", parsedBody);
		User user = new User(
			parsedBody.get("userId"),
			parsedBody.get("password"),
			parsedBody.get("name"),
			parsedBody.get("email")
		);
		saveUser(user, response);
	}

	private void saveUser(User user, Response response) {
		if (DataBase.validateDuplicatedId(user)) {
			DataBase.addUser(user);
			response.newResponse302("http://localhost:8080/index.html");
			return;
		}
		response.newResponse302("http://localhost:8080/user/form.html");
	}
}
