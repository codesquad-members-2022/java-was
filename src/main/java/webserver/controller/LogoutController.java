package webserver.controller;

import db.SessionDataBase;
import webserver.Request;
import webserver.Response;
import webserver.StatusCode;

import java.io.IOException;
import java.util.Optional;

public class LogoutController implements Controller {

	private static final LogoutController instance = new LogoutController();

	private LogoutController() {
		super();
	}

	public static LogoutController getInstance() {
		return instance;
	}

	@Override
	public void process(Request request, Response response) throws IOException {
		// 쿠키 가져오기
		Optional<String> cookie = request.getCookieValue("sessionId");

		// session DB 삭제
		SessionDataBase.deleteByCookie(cookie.orElse(""));

		// 응답 헤더 설정
		response.setDeleteCookie();
		response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/index.html");
	}
}
