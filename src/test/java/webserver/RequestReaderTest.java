package webserver;

import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestReaderTest {

    @Test
    @DisplayName("RequestReader 가 생성된다")
    public void createRequestReaderTest() throws IOException {
        // given
        String newLine = System.getProperty("line.separator");

        InputStream is = new ByteArrayInputStream(String.join(newLine,
                        "GET /index.html HTTP/1.1", "Accept: */*", newLine)
                .getBytes(StandardCharsets.UTF_8));

        RequestReader requestReader = new RequestReader(is);

        // when
        Request request = requestReader.create();

        // then
        then(request.getMethod()).isEqualTo("GET");
        then(request.getUrl()).isEqualTo("/index.html");
        then(request.getProtocol()).isEqualTo("HTTP/1.1");
        then(request.getHeader()).contains(entry("Accept", "*/*"));
    }
}
