package model.http.session;

import configuration.ObjectFactory;
import model.handler.Handler;
import model.handler.controller.UserLoginController;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import webserver.SessionDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CookieTest {

    private SessionDatabase sessionDatabase;
    private User user;

    private User getUser() {
        return new User("meen", "zino", "송민제", "min@gmail.com");
    }

    @BeforeEach
    void init() {
        sessionDatabase = ObjectFactory.sessionDatabase;
        user = getUser();
    }

    /**
     * 만료된 쿠키의 테스트는 어떻게 되는지?
     */
    @Test
    @DisplayName("쿠키를 저장하면 세션데이터베이스에 저장된다.")
    void createCookie() {
        String userId = "meenzino";
        Cookie savedCookie = SessionDatabase.createCookie(userId);

        Cookie expectedCookie = sessionDatabase.getCookie(userId);
        int expectedSize = 1;

        assertThat(expectedCookie).isEqualTo(savedCookie);
        assertThat(expectedSize).isEqualTo(sessionDatabase.size());
    }

    @Nested
    @DisplayName("로그인이 된 사용자는")
    class LoginUser {

        @Nested
        @DisplayName("저장된 세션값을 통해")
        class SessionUser {

            @Mock
            private InputStream inputStream;

            @Mock
            private OutputStream outputStream;

            private Handler handler;

            @BeforeEach
            void init() {
                handler = Mockito.mock(UserLoginController.class);
            }

            @Test
            @DisplayName("해당 사용자를 식별할 수 있다.")
            void 해당_사용자를_식별할_수_있다() throws IOException {
                /**
                 *
                 * */
            }
        }
    }
}
