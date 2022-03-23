package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.BDDAssertions.then;

class RequestTest {


    @Test
    @DisplayName("Request 객체가 생성된다")
    void createRequestTest() throws IOException {
        // given
        String line = "GET /index.html HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Accept", "*/*");
        }};

        // when
        Request request = new Request(line, header);

        // then
        then(request.getMethod()).isEqualTo("GET");
        then(request.getUrl()).isEqualTo("/index.html");
        then(request.getProtocol()).isEqualTo("HTTP/1.1");
        then(request.getHeader().get("Accept")).isEqualTo("*/*");
    }

    @Test
    @DisplayName("request 객체로부터 확장자가 반환된다")
    void parseExtTest() throws IOException {
        // given
        String line = "GET /index.html HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Accept", "*/*");
        }};

        Request request = new Request(line, header);

        // when
        String ext = request.parseExt();

        // then
        then(ext).isEqualTo("html");
    }
}
