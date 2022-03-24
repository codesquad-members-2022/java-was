package http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HttpResponseTest {

    @Test
    void createResponseTest() {
        //given
        String statusLine = "HTTP/1.1 200 OK";
        byte[] body = "<h1> My Home Page </h1>".getBytes(StandardCharsets.UTF_8);


        //when
        HttpResponse response = new HttpResponse.Builder()
                .status(statusLine)
                .setHeader("Content-length", "35")
                .setHeader("Content-type", "text/html")
                .body(body)
                .build();

        //then
        assertThat(response.getStatusLine()).isEqualTo("HTTP/1.1 200 OK");
        assertThat(response.getHeader().get("Content-length")).isEqualTo("35");
        assertThat(response.getHeader().get("Content-type")).isEqualTo("text/html");
        assertThat(response.getBody()).isEqualTo("<h1> My Home Page </h1>");
    }
}
