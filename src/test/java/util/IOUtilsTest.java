package util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;

public class IOUtilsTest {
    private static final Logger logger = LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws Exception {
        String data = "abcd123";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.debug("parse body : {}", IOUtils.readData(br, data.length()));
    }

    @Test
    @DisplayName("request header가 들어오면 List<Pair> 값을 반환한다.")
    void readRequestHeader() throws IOException {
        //given
        String requestHeader = "Host: localhost:8080\r\n"
            + "User-Agent: curl/7.79.1\r\n"
            + "Accept: */*\r\n"
            + "\r\n";
        InputStream is = new ByteArrayInputStream(requestHeader.getBytes());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        //when
        List<Pair> pairs = IOUtils.readRequestHeader(br);

        //then
        assertThat(pairs).hasSize(3);
        assertThat("Host: localhost:8080").isEqualTo(pairs.get(0).toString());
        assertThat("User-Agent: curl/7.79.1").isEqualTo(pairs.get(1).toString());
        assertThat("Accept: */*").isEqualTo(pairs.get(2).toString());
    }
}
