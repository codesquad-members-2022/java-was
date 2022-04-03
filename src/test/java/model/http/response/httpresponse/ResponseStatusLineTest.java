package model.http.response.httpresponse;

import model.http.HttpStatusCode;
import model.http.HttpVersion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ResponseStatusLineTest {

    @Test
    @DisplayName("응답 성공 시 HTTP/1.1 200 OK 응답을 반환한다.")
    void responseStatusLine() {
        String expected = "HTTP/1.1 200 OK" + "\r\n";
        ResponseStatusLine responseStatusLine = new ResponseStatusLine();
        HttpVersion version = HttpVersion.HTTP_1_1;
        HttpStatusCode statusCode = HttpStatusCode.OK;

        assertThat(expected).isEqualTo(responseStatusLine.get200StatusStatusLine(version, statusCode));
    }

}
