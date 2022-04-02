package model.http;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HttpMethodTest {
    @Test
    @DisplayName("존재하는 HttpMethod를 넣으면 알맞는 메서드가 나온다.")
    void findHttpMethod_success() throws Exception {
        HttpMethod getMethod = HttpMethod.GET;
        HttpMethod postMethod = HttpMethod.POST;

        HttpMethod expectedGet = HttpMethod.getMethod("GET");
        HttpMethod expectedPost = HttpMethod.getMethod("POST");

        Assertions.assertThat(expectedGet).isEqualTo(getMethod);
        Assertions.assertThat(expectedPost).isEqualTo(postMethod);
    }

    @Test
    @DisplayName("존재하지 않는 HttpMethod를 넣으면 null이 반환된다.")
    void findHttpMethod_fail() throws Exception {
        HttpMethod getMethod = HttpMethod.getMethod("PUT");

        HttpMethod expectedPut = null;

        Assertions.assertThat(expectedPut).isEqualTo(getMethod);
    }

}
