package model.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ContentTypeTest {

    @Test
    @DisplayName("존재하는 Content-Type을 넣으면 해당하는 Content-Type이 나온다.")
    void findHttpMethod_success() {
        String expectedContentTypeHtml = ContentType.HTML.getMimeType();
        String expectedContentTypeCss = ContentType.CSS.getMimeType();
        String expectedContentTypeJavascript = ContentType.JAVASCRIPT.getMimeType();
        String expectedContentTypeIco = ContentType.ICO.getMimeType();
        String expectedContentTypeWoff = ContentType.WOFF.getMimeType();
        String expectedContentTypeNONE = ContentType.OTHER.getMimeType();

        assertThat(expectedContentTypeHtml).isEqualTo(ContentType.of("text/html;"));
        assertThat(expectedContentTypeCss).isEqualTo(ContentType.of("text/css;"));
        assertThat(expectedContentTypeJavascript).isEqualTo(ContentType.of("javascript"));
        assertThat(expectedContentTypeIco).isEqualTo(ContentType.of("image/x-icon;"));
        assertThat(expectedContentTypeWoff).isEqualTo(ContentType.of("application/font-woff;"));
        assertThat(expectedContentTypeNONE).isEqualTo(ContentType.of(""));
    }

    @Test
    @DisplayName("존재하지 않는 Content-Type을 넣으면 NONE이 나온다.")
    void findHttpMethod_fail() {
        String expectedContentTypeAll = ContentType.OTHER.getMimeType();
        assertThat(expectedContentTypeAll).isEqualTo(ContentType.of("TEST"));
    }
}
