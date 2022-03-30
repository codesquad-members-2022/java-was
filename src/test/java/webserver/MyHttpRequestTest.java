package webserver;

import java.io.IOException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyHttpRequestTest {

    String requestMessage = "POST /index.html?userId=test&password=hello HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Accept: text/html,*/*\n"
            + "\n"
            + "name=spring&age=20\n";

    InputStream is = new ByteArrayInputStream(requestMessage.getBytes());
    MyHttpRequest request;

    @BeforeEach
    void setUp() {
        try {
            request = new MyHttpRequest(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void startLineTest() {

        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getRequestURI()).isEqualTo("/index.html");
        assertThat(request.getParamMap().get("userId")).isEqualTo("test");
        assertThat(request.getParamMap().get("password")).isEqualTo("hello");
        assertThat(request.getProtocol()).isEqualTo("HTTP/1.1");
    }

    @Test
    void headerTest() {

        String[] hostValues = {"localhost:8080"};
        String[] connectionValues = {"keep-alive"};
        String[] acceptValues = {"text/html", "*/*"};


        assertThat(request.getHeader("Host")).isEqualTo(hostValues);
        assertThat(request.getHeader("Connection")).isEqualTo(connectionValues);
        assertThat(request.getHeader("Accept")).isEqualTo(acceptValues);
    }

    @Test
    void BodyTest() {

        assertThat(request.getParamMap().get("name")).isEqualTo("spring");
        assertThat(request.getParamMap().get("age")).isEqualTo("20");
    }

}
