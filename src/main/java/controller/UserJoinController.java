package controller;

import db.DataBase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
		List<Pair> pairs = new ArrayList<>();
		if (DataBase.validateDuplicatedId(user)) {
			DataBase.addUser(user);
			log.debug("SavedUser: {}", user);
			pairs.add(new Pair("Location", "http://localhost:8080/index.html"));
			response.write(HttpStatus.FOUND, pairs);
			return;
		}
		log.debug("Save Fail: {}", user);
		pairs.add(new Pair("Location", "http://localhost:8080/user/form.html"));
		response.write(HttpStatus.FOUND, pairs);
	}
}
