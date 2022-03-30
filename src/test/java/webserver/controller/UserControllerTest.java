package webserver.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import webserver.HttpResponse;
import webserver.HttpStatus;

class UserControllerTest {
	private UserController userController;

	@BeforeEach
	void beforeEach() {
		this.userController = new UserController();
	}

	@DisplayName("회원가입 요청시 새로운 사용자는 회원가입 진행되어 리다이렉트 응답코드 302를 확인한다")
	@Test
	void check_status_302_when_new_user_join() {
		HttpResponse actual = userController.join(getUser(), getHttpResponse());

		assertThat(actual.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
	}

	@DisplayName("회원가입 요청시 중복요청하는 사용자의 회원가입 진행시에는 200OK로 가입 페이지로 이동됨을 확인한다")
	@Test
	void check_status_200OK_when_new_user_duplicated_join() {
		userController.join(getUser(), getHttpResponse());

		HttpResponse actual = userController.join(getUser(), getHttpResponse());
		assertThat(actual.getHttpStatus()).isEqualTo(HttpStatus.OK);
	}

	private Map<String, String> getUser() {
		return Map.of("userId","tester"
		,"password","asdf",
			"name","testing",
			"email","test@email.com");
	}

	private HttpResponse getHttpResponse() {
		return new HttpResponse("HTTP/1.1");
	}

}
