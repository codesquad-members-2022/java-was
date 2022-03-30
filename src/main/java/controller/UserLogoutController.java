package controller;

import java.io.IOException;
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
		response.logoutHeader("http://localhost:8080/index.html");
	}

}
