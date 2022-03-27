package web.request;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RequestLineTest {

    @Test
    @DisplayName("GET 메서드를 정상적으로 반환하는 지 테스트")
    void getMethodTest() {
        // given
        String requestLineString = "GET /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        HttpMethod method = requestLine.getMethod();

        // then
        assertThat(method).isSameAs(HttpMethod.GET);
    }

    @Test
    @DisplayName("POST 메서드를 정상적으로 반환하는 지 테스트")
    void postMethodTest() {
        // given
        String requestLineString = "POST /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        HttpMethod method = requestLine.getMethod();

        // then
        assertThat(method).isSameAs(HttpMethod.POST);
    }

    @Test
    @DisplayName("프로토콜을 정상적으로 반환하는 지 테스트")
    void protocolTest() {
        // given
        String requestLineString = "GET /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        String protocol = requestLine.getProtocol();

        // then
        assertThat(protocol).isEqualTo("HTTP/1.1");
    }

    @Test
    @DisplayName("queryString이 없는 경우 path를 정상적으로 반환하는 지 테스트")
    void no_queryString_pathTest() {
        // given
        String requestLineString = "GET /index.html HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        String path = requestLine.getPath();

        // then
        assertThat(path).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("queryString이 있 경우 path를 정상적으로 반환하는 지 테스트")
    void has_queryString_pathTest() {
        // given
        String requestLineString = "GET /index.html?name=땃쥐 HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        String path = requestLine.getPath();

        // then
        assertThat(path).isEqualTo("/index.html");
    }

    @Test
    @DisplayName("쿼리 파라미터들을 정상적으로 반환하는 지 테스트")
    void queryParametersTest() {
        // given
        String requestLineString = "GET /index.html?name=땃쥐&age=20 HTTP/1.1";
        RequestLine requestLine = new RequestLine(requestLineString);

        // when
        Map<String, String> queryParameters = requestLine.getQueryParameters();

        // then
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(queryParameters.size()).isEqualTo(2);
        softAssertions.assertThat(queryParameters.get("name")).isEqualTo("땃쥐");
        softAssertions.assertThat(queryParameters.get("age")).isEqualTo("20");
        softAssertions.assertAll();
    }
}
