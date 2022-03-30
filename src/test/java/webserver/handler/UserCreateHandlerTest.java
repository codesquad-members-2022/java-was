package webserver.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Request;
import webserver.Response;
import webserver.Status;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;


class UserCreateHandlerTest {

    Map<String, String> headers;
    UserCreateHandler handler;

    @BeforeEach
    public void setUp() {
        headers = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        handler = new UserCreateHandler();
    }

    @Test
    @DisplayName("Request 객체가 입력되었을 때 User 객체를 생성하고 Response 객체를 반환한다")
    void userCreateHandlerTest() {
        // given
        String line = "POST /user/create HTTP/1.1";
        Map<String, String> body = new HashMap<>() {{
            put("userId", "javajigi");
            put("password", "password");
            put("name", "박재성");
            put("email", "javajigi@slipp.net");
        }};
        Request request = new Request(line, headers, body);

        // when
        Response response = handler.handle(request);

        // then
        then(response.getStatus()).isEqualTo(Status.FOUND);
        then(response.getHeader()).containsExactlyInAnyOrderEntriesOf(
                new HashMap<>() {{
                    put("Location", "http://localhost:8080/index.html");
                }}
        );
    }

}
