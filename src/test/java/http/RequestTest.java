package http;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Request 테스트")
class RequestTest {

    @Nested
    @DisplayName("Request 객체는")
    class RequestObjectTest {

        @Nested
        @DisplayName("GET 요청이면")
        class GetTest{

            @Test
            void requestLine_의_queryString_이_parameters_에_추가된다() {
                // given
                String queryString = "key1=value1&key2=value2";
                int expectedParameterSize = 2;
                HttpRequestLine requestLine = new HttpRequestLine();
                requestLine.setHttpMethod(HttpMethod.GET);
                requestLine.setQueryString(queryString);

                HttpRequestData requestData = new HttpRequestData();
                requestData.setHttpRequestLine(requestLine);

                // when
                Request request = Request.of(requestData);

                // then
                assertThat(request).isNotNull();
                assertThat(request.getHttpMethod()).isEqualTo(HttpMethod.GET);
                assertThat(request.getParameters()).isNotNull();
                assertThat(request.getParameters()).size().isEqualTo(expectedParameterSize);
            }
        }
    }
}

