package util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MimeUtilsTest {

    @Test
    void convertToContentType() {
        String url1 = "/index.html";
        String url2 = "/bootstrap.min.js";
        String contentType1 = MimeUtils.convertToContentType(url1);
        String contentType2 = MimeUtils.convertToContentType(url2);
        assertThat(contentType1).isEqualTo("text/html;charset=utf-8");
        assertThat(contentType2).isEqualTo("application/javascript");
    }
}
