package webserver;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;

class HttpRequestTest {

    @Test
    @DisplayName("파라미터 네임을 전달하면 대응되는 파라미터 값을 반환한다.")
    void getParameterTest() throws IOException {
        //given 준비
        String requestLine = "GET /user/create?userId=qqq&password=www&name=eee&email=rrr%40rrr HTTP/1.1";
        String headLine = "Host: localhost:8080";
        List<Pair> headers = new ArrayList<>();
        Pair header = HttpRequestUtils.parseHeader(headLine);
        headers.add(header);

        //when 실행
        //Todo given으로 올리기
        HttpRequest httpRequest = new HttpRequest(requestLine, headers);

        String result = httpRequest.getParameter("userId");

        //then 검증
        assertThat(result).isEqualTo("qqq");
    }
}
