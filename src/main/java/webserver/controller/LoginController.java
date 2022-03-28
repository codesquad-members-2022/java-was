package webserver.controller;

import db.DataBase;
import java.io.IOException;

import db.SessionDataBase;
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
			/**
			 todo
			 1. sessionDb에 넣고
			 2. 응답에 넣어주고
			 3. Path=/ : 쿠키를 어디에 사용할지 정한다.  / 이후 모두...
			 */
			String cookie = SessionDataBase.addSession(requestUserId);
			response.setCookie(cookie);
			response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/index.html");
		} else{
			//로그인 실패 처리
			response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/user/login_failed.html");
		}

	}
}
