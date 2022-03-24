package model;

import http.HttpRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpRequestTest {

    @Test
    @DisplayName("Request 객체가 생성된다")
    void createRequestTest() {
        // given
        Map<String, String> header = new HashMap<>() {{
            put("Accept", "*/*");
            put("Content-Length", "35");
        }};

        // when
        HttpRequest request = new HttpRequest.Builder().path("/create")
                .method("POST")
                .protocol("HTTP/1.1")
                .body("뽀로로 케이")
                .header(header).build();

        // then
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo("/create");
        assertThat(request.getProtocol()).isEqualTo("HTTP/1.1");
        assertThat(request.getBody()).isEqualTo("뽀로로 케이");
        assertThat(request.getHeader().get("Accept")).isEqualTo("*/*");
        assertThat(request.getHeader().get("Content-Length")).isEqualTo("35");
    }
}
