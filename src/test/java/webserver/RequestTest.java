package webserver;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestTest {

    @Test
    @DisplayName("Request 객체가 생성된다")
    void createRequestTest() {
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
    @DisplayName("Request 객체의 url 에 query string 이 포함됐을 때 path 가 분리된다")
    void parsePathTest() {
        // given
        String line = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        Request request = new Request(line, header);

        // when
        String path = request.parsePath();

        // then
        then(path).isEqualTo("/create");
    }

    @Test
    @DisplayName("Request 객체의 url 에서 query string 이 포함됐을 때 query string 이 분리된다")
    void parseQueryStringTest() {
        // given
        String line = "GET /create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        Request request = new Request(line, header);

        // when
        Map<String, String> queryString = request.parseQueryString();

        // then
        then(queryString).containsExactlyInAnyOrderEntriesOf(
            new HashMap<>() {{
                put("userId", "javajigi");
                put("password", "password");
                put("name", "박재성");
                put("email", "javajigi@slipp.net");
            }}
        );
    }

    @Test
    @DisplayName("Request 객체의 url 에서 query string 이 포함되지 않았을 때 빈 Map 을 반환한다")
    void parseEmptyQueryStringTest() {
        // given
        String line = "GET /create HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        Request request = new Request(line, header);

        // when
        Map<String, String> queryString = request.parseQueryString();

        // then
        then(queryString).containsExactlyInAnyOrderEntriesOf(new HashMap<>());
    }

    @Test
    @DisplayName("html 확장자를 가진 요청이 들어오면 Content-Type 으로 text/html 을 반환한다")
    void toContentTypeTest() {
        // given
        String line = "GET /index.html HTTP/1.1";
        Map<String, String> header = new HashMap<>() {{
            put("Host", "localhost:8080");
            put("Connection", "keep-alive");
            put("Accept", "*/*");
        }};

        Request request = new Request(line, header);

        // when
        String contentType = request.toContentType();

        // then
        then(contentType).isEqualTo("text/html");
    }

}
