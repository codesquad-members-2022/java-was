package webserver.handler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Request;
import webserver.Response;
import webserver.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

class PathMapperTest {

    Map<String, String> headers;
    PathMapper pathMapper;

    @BeforeEach
    public void setUp() {
        headers = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        pathMapper = new PathMapper();
    }

    @Test
    @DisplayName("Request 객체가 입력되었을 때 DefaultFileHandler 의 handle 을 통해 Response 객체가 반환된다")
    void pathMapperToDefaultFileHandlerTest() throws IOException {
        // given
        String line = "GET /index.html HTTP/1.1";
        Request request = new Request(line, headers);

        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());

        // when
        Response response = pathMapper.callHandler(request);

        // then
        then(response.getStatus()).isEqualTo(Status.OK);
        then(response.getBody()).isEqualTo(body);
        then(response.getHeader()).containsExactlyInAnyOrderEntriesOf(
                new HashMap<>() {{
                    put("Content-Type", "text/html");
                    put("Content-Length", String.valueOf(body.length));
                }}
        );
    }

    @Test
    @DisplayName("Request 객체가 입력되었을 때 UserCreateHandler 의 handle 을 통해 Response 객체가 반환된다")
    void pathMapperToCreateUserHandlerTest() {
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
        Response response = pathMapper.callHandler(request);

        // then
        then(response.getStatus()).isEqualTo(Status.FOUND);
        then(response.getHeader()).containsExactlyInAnyOrderEntriesOf(
                new HashMap<>() {{
                    put("Location", "http://localhost:8080/index.html");
                }}
        );
    }

}
