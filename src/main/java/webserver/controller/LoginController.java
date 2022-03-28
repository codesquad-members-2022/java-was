package webserver.controller;

import db.DataBase;
import java.io.IOException;
import model.User;
import webserver.Request;
import webserver.Response;
import webserver.StatusCode;

public class LoginController extends Controller{
	private static final LoginController instance = new LoginController();

	private LoginController() {
		super();
	}

	public static LoginController getInstance(){
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		String requestUserId = request.getParam("userId");
		String requestPassword = request.getParam("password");

		User user = DataBase.findUserById(requestUserId);
		if(user != null && user.getPassword().equals(requestPassword)){
			//로그인 처리
			response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/index.html");
		} else{
			//로그인 실패 처리
			response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/user/login_failed.html");
		}

	}
}
