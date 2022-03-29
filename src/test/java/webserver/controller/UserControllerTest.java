package webserver.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.HttpResponse;
import webserver.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

    @DisplayName("로그인 요청 시 성공하면 응답에 쿠키가 포함된다")
    @Test
    void check_cookie_when_login_success() {
        userController.join(getUser(), getHttpResponse());
        Map<String, String> loginBody = getLoginBody("tester", "asdf");
        String userId = loginBody.get("userId");
        HttpResponse httpResponse = userController.login(loginBody, getHttpResponse());
        assertThat(httpResponse.getHeader("Set-Cookie")).isEqualTo("userId=" + userId + "; Path=/");
        assertThat(httpResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }

    @DisplayName("로그인 요청 실패 시 실패 페이지로 리다이렉트된다")
    @Test
    void check_login_failed_page_when_login_failed() {
        userController.join(getUser(), getHttpResponse());
        Map<String, String> loginBody1 = getLoginBody("wrongId", "asdf");
        Map<String, String> loginBody2 = getLoginBody("tester", "wrong");

        HttpResponse actualUserId = userController.login(loginBody1, getHttpResponse());
        assertThat(actualUserId.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(actualUserId.getHeader("Location")).isEqualTo("/user/login_failed.html");

        HttpResponse actualPassword = userController.login(loginBody2, getHttpResponse());
        assertThat(actualPassword.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
        assertThat(actualPassword.getHeader("Location")).isEqualTo("/user/login_failed.html");
    }

    private HttpResponse getHttpResponse() {
        return new HttpResponse("HTTP/1.1");
    }

    private Map<String, String> getUser() {
        return Map.of("userId", "tester"
                , "password", "asdf",
                "name", "testing",
                "email", "test@email.com");
    }

    private Map<String, String> getLoginBody(String userId, String password) {
		return Map.of("userId", userId, "password", password);
    }
}
