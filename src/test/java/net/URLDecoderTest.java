package net;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class URLDecoderTest {

    @Test
    @DisplayName("decode메서드는 넘어온 URL을 UTF-8로 디코딩할 수 있어야 한다.")
    public void decodeTest() throws Exception {
        //given
        String urlEncodedString = "userId=%EB%95%83%EB%95%83%EB%95%83";

        //when
        String decodedString = URLDecoder.decode(urlEncodedString, StandardCharsets.UTF_8);

        //then
        assertThat(decodedString).contains("땃땃땃");
    }
}
