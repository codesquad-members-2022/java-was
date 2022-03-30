package webserver.controller;

import org.junit.jupiter.api.Test;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;

class UrlMapperTest {

    @Test
    void getResponse() {
        //given
        Map<String,String> tempMap= Map.of(
                "Cookie","sessionId=445a75ce-bcd9-49a3-a233-cbc687c4ec3d",
                "Accept", "text/html",
                "Connection", "keep-alive",
                "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.82 Safari/537.36",
                "Accept-Encoding","gzip, deflate, br",
                "Sec-Fetch-Mode","no-cors",
                "Accept-Language","ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7"
        );
        HttpRequest request = new HttpRequest("GET", "/index.html", "HTTP/1.1", tempMap, null, false);
        HttpResponse response = new HttpResponse("HTTP/1.1", HttpStatus.OK);

        //when
        HttpResponse returnResponse = UrlMapper.getResponse(request);

        //then
        assertThat(returnResponse.getHttpStatusCode()).isEqualTo(response.getHttpStatusCode());
    }
}
