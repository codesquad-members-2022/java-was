package model.http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpVersionTest {

    @Test
    @DisplayName("해당하는 HttpVersion이 존재하지 않으면 IllegalArgumentException을 발생시킨다.")
    void findHttpVersion_Fail() {
        assertThrows(IllegalArgumentException.class, () -> HttpVersion.of("HTTP/10000"));
    }

    @Test
    @DisplayName("해당하는 HttpVersion이 존재하면 해당 버전을 반환한다.")
    void findHttpVersion_SUCCESS() {
        HttpVersion version = HttpVersion.of("HTTP/1.1");
        assertThat(version).isEqualTo(HttpVersion.HTTP_1_1);
    }
}
