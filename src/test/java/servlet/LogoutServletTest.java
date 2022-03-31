package servlet;

import http.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LogoutServlet 테스트(로그아웃 기능)")
class LogoutServletTest {

    private final LogoutServlet logoutServlet = new LogoutServlet();

    @Nested
    @DisplayName("로그아웃 요청이 들어왔을때")
    class LogoutRequestTest {

        @Nested
        @DisplayName("로그인 상태이면")
        class LoggedInStateTest {

            @Test
            @DisplayName("로그아웃 작업을 수행하고 /index.html 로 리다이렉트 된다")
            void logoutAndRedirect() {
                // given
                String expectedRedirectUrl = "/index.html";
                String cookieName = "sessionId";
                int cookieMaxAge = 0;
                String cookiePath = "/";
                Request request = new Request();
                request.setSessionId(Session.createSession());
                Response response = new Response();

                // when
                Response logoutResponse = logoutServlet.doGet(request, response);

                // then
                assertThat(logoutResponse).isNotNull();
                assertThat(logoutResponse.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
                assertThat(logoutResponse.getRedirectUrl()).isEqualTo(expectedRedirectUrl);

                Optional<Cookie> parseResult = parseSessionIdCookieFromResponse(logoutResponse);
                assertThat(parseResult.isPresent()).isTrue();
                Cookie sessionIdCookie = parseResult.orElseThrow();
                assertThat(sessionIdCookie.getName()).isEqualTo(cookieName);
                assertThat(sessionIdCookie.getMaxAge()).isEqualTo(cookieMaxAge);
                assertThat(sessionIdCookie.getPath()).isEqualTo(cookiePath);
            }
        }

        @Nested
        @DisplayName("로그인 상태가 아니면")
        class NotLoggedInStateTest {

            @Test
            @DisplayName("아무런 작업 없이 /index.html 로 리다이렉트 된다")
            void justRedirect() {
                // given
                String expectedRedirectUrl = "/index.html";
                Request request = new Request();
                Response response = new Response();

                // when
                Response logoutResponse = logoutServlet.doGet(request, response);

                // then
                assertThat(logoutResponse).isNotNull();
                assertThat(logoutResponse.getRedirectUrl()).isEqualTo(expectedRedirectUrl);
                Optional<Cookie> parseResult = parseSessionIdCookieFromResponse(logoutResponse);
                assertThat(parseResult.isEmpty()).isTrue();
            }
        }

        private Optional<Cookie> parseSessionIdCookieFromResponse(Response loginResult) {
            return loginResult.getCookies()
                .stream()
                .filter(cookie -> cookie.getName().equals("sessionId"))
                .findFirst();
        }
    }
}

