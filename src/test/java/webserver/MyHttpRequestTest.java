package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MyHttpRequestTest {

    String requestMessage = "GET /index.html&userId=test&password=hello HTTP/1.1\n"
            + "Host: localhost:8080\n"
            + "Connection: keep-alive\n"
            + "Accept: text/html,*/*\n";

    InputStream is = new ByteArrayInputStream(requestMessage.getBytes());
    MyHttpRequest request = new MyHttpRequest(is);

    @Test
    void startLineTest() {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userId", "test");
        paramMap.put("password", "hello");

        assertThat(request.getMethod()).isEqualTo("GET");
        assertThat(request.getRequestURI()).isEqualTo("/index.html");
        assertThat(request.getParamMap()).isEqualTo(paramMap);
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

}
