package webserver.controller;

import db.DataBase;
import java.io.IOException;

import db.SessionDataBase;
import model.User;
import webserver.Request;
import webserver.Response;
import webserver.StatusCode;

public class LoginController implements Controller {

	private static final LoginController instance = new LoginController();

	private LoginController() {
		super();
	}

	public static LoginController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		String requestUserId = request.getParam("userId");
		String requestPassword = request.getParam("password");

		User user = DataBase.findUserById(requestUserId);
		if (isLogin(requestPassword, user)) {
			String cookie = SessionDataBase.addSession(requestUserId);
			response.setCookie(cookie);
			response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/index.html");
			return;
		}
		response.setRedirect(StatusCode.REDIRECTION_302,
			"http://localhost:8080/user/login_failed.html");

}

	private boolean isLogin(String requestPassword, User user) {
		return user != null && user.getPassword().equals(requestPassword);
	}
}
