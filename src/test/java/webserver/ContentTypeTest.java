package webserver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class ContentTypeTest {

    @Test
    @DisplayName("html 확장자가 입력되면 ContentType 의 HTML 상수가 반환된다")
    void htmlToContentTypeTest() {
        // when
        ContentType contentType = ContentType.from("html");

        // then
        then(contentType).isEqualTo(ContentType.HTML);
    }

    @Test
    @DisplayName("지정되지 않은 확장자가 입력되면 ContentType 의 OTHER 상수가 반환된다")
    void otherToContentTypeTest() {
        // when
        ContentType contentType = ContentType.from("ico");

        // then
        then(contentType).isEqualTo(ContentType.OTHER);
    }

}
