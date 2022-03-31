package servlet;

import db.DataBase;
import http.HttpStatus;
import http.Request;
import http.Response;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginServlet 테스트(로그인 기능)")
class LoginServletTest {

    private final LoginServlet loginServlet = new LoginServlet();

    @BeforeEach
    void setup() {
        DataBase.deleteAll();
    }

    @Nested
    @DisplayName("가입된 아이디와 비밀번호가 들어왔을 때")
    class CorrectDataTest {

        @Nested
        @DisplayName("비밀번호가 같으면")
        class SamePasswordTest {

            @Test
            @DisplayName("로그인에 성공하고 /index.html 로 리다이렉트 된다")
            void loginSuccess() {
                // given
                String expectedRedirectUrl = "/index.html";
                User savedUser = getUser();
                registerUser(savedUser);

                Request request = new Request();
                request.addParameters(new HashMap<>(){{
                    put("userId", savedUser.getUserId());
                    put("password", savedUser.getPassword());
                }});
                Response response = new Response();

                // when
                Response loginResponse = loginServlet.doPost(request, response);

                // then
                assertThat(loginResponse).isNotNull();
                assertThat(loginResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
                assertThat(loginResponse.getRedirectUrl()).isEqualTo(expectedRedirectUrl);
            }
        }

        @Nested
        @DisplayName("비밀번호가 다르면")
        class DifferentPasswordTest {

            @Test
            @DisplayName("로그인에 실패하고 /user/login_failed.html 로 리다이렉트 된다")
            void test() {
                // given
                String expectedRedirectUrl = "/user/login_failed.html";
                User savedUser = getUser();
                registerUser(savedUser);

                Request request = new Request();
                request.addParameters(new HashMap<>(){{
                    put("userId", savedUser.getUserId());
                    put("password", savedUser.getPassword() + "different");
                }});
                Response response = new Response();

                // when
                Response loginResponse = loginServlet.doPost(request, response);

                // then
                assertThat(loginResponse).isNotNull();
                assertThat(loginResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
                assertThat(loginResponse.getRedirectUrl()).isEqualTo(expectedRedirectUrl);
            }
        }

        private void registerUser(User user) {
            DataBase.addUser(user);
        }
    }

    @Nested
    @DisplayName("가입된 아이디가 아니면")
    class NotRegisteredTest {
        @Test
        @DisplayName("로그인이 되지 않고 /user/login_failed.html 로 리다이렉트 된다")
        void loginFailed() {
            // given
            String expectedRedirectUrl = "/user/login_failed.html";
            Request request = new Request();
            request.addParameters(new HashMap<>(){{
                put("userId", "id");
                put("password", "pw");
            }});
            Response response = new Response();

            // when
            Response loginResponse = loginServlet.doPost(request, response);

            // then
            assertThat(loginResponse).isNotNull();
            assertThat(loginResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
            assertThat(loginResponse.getRedirectUrl()).isEqualTo(expectedRedirectUrl);
        }
    }

    private User getUser() {
        return new User("jwkim", "1234", "김진완", "jwkim@naver.com");
    }
}
