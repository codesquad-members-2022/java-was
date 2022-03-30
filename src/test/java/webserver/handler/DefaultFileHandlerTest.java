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

class DefaultFileHandlerTest {

    Map<String, String> headers;
    DefaultFileHandler handler;

    @BeforeEach
    public void setUp() {
        headers = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        handler = new DefaultFileHandler();
    }

    @Test
    @DisplayName("Request 객체가 입력되었을 때 파일을 읽어 Response 객체로 구성된다")
    void defaultFileHandlerTest() throws IOException {
        // given
        String line = "GET /index.html HTTP/1.1";
        Request request = new Request(line, headers);

        byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());

        // when
        Response response = handler.handle(request);

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
}
