package webserver;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import config.RequestMapping;
import db.DataBase;
import http.HttpMethod;
import http.HttpServlet;
import http.HttpStatus;
import http.Request;
import http.Response;
import webserver.dto.HttpRequestData;
import webserver.dto.HttpRequestLine;


@DisplayName("Dispatcher에서")
class DispatcherTest {

    private Dispatcher dispatcher;

    private HttpRequestData requestData;

    @BeforeEach
    void setUp() {
        RequestMapping requestMapping = new RequestMapping.Builder()
            .addMapping("/get/stub", new GetStub())
            .addMapping("/post/stub", new PostStub())
            .build();
        dispatcher = new Dispatcher(requestMapping);
    }

    @Nested
    @DisplayName("유효한 url mapping이며")
    class Valid_url {

        @Nested
        @DisplayName("체크 요청일 경우")
        class If_Check_Request {

            @DisplayName("true를 반환한다.")
            @Test
            void valid_url_return_true() {
                // when
                boolean result = dispatcher.isMappedUrl("/get/stub");

                // then
                assertThat(result).isTrue();
            }
        }

        @Nested
        @DisplayName("GET으로 Mapping 된 상태에서")
        class Mapping_with_GET_method {

            @DisplayName("POST 요청이 올 경우 예외가 발생한다.")
            @Test
            void valid_GET_url_POST_request_throw_exception() {
                // given
                requestData = new HttpRequestData();
                HttpRequestLine httpRequestLine = new HttpRequestLine();
                httpRequestLine.setUrl("/get/stub");
                requestData.setHttpRequestLine(httpRequestLine);
                requestData.getHttpRequestLine().setHttpMethod(HttpMethod.POST);

                // when & then
                assertThrows(IllegalStateException.class, () -> {
                    dispatcher.handleRequest(requestData);
                });
            }

            @DisplayName("GET 요청이 올 경우 OK 상태가 반환된다.")
            @Test
            void valid_GET_url_request() {
                // given
                requestData = new HttpRequestData();
                HttpRequestLine httpRequestLine = new HttpRequestLine();
                httpRequestLine.setUrl("/get/stub");
                requestData.setHttpRequestLine(httpRequestLine);
                requestData.getHttpRequestLine().setHttpMethod(HttpMethod.GET);

                // when
                Response response = dispatcher.handleRequest(requestData);

                // then
                assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.OK);
            }

        }
    }

    @DisplayName("유효하지 않은 url mapping에 대한 체크 요청일 경우, false를 반환한다.")
    @Test
    void invalid_url_return_false() {
        // when
        boolean result = dispatcher.isMappedUrl("/user/random");

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("유효하지 않은 url에 대한 요청이 올 경우 예외가 발생한다.")
    @Test
    void invalid_url_throw_exception() {
        // given
        requestData = new HttpRequestData();
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        httpRequestLine.setUrl("/user/random");
        requestData.setHttpRequestLine(httpRequestLine);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            dispatcher.handleRequest(requestData);
        });
    }

    @DisplayName("유효한 POST Mapping url에 대해 GET 메서드로 요청이 올 경우 예외가 발생한다.")
    @Test
    void valid_POST_url_GET_request_throw_exception() {
        // given
        requestData = new HttpRequestData();
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        httpRequestLine.setUrl("/post/stub");
        requestData.setHttpRequestLine(httpRequestLine);
        requestData.getHttpRequestLine().setHttpMethod(HttpMethod.GET);

        // when & then
        assertThrows(IllegalStateException.class, () -> {
            dispatcher.handleRequest(requestData);
        });
    }

    @DisplayName("유효한 POST Mapping url에 대해 POST 메서드로 요청이 올 경우 FOUND 상태가 반환된다.")
    @Test
    void valid_POST_url_request() {
        // given
        requestData = new HttpRequestData();
        HttpRequestLine httpRequestLine = new HttpRequestLine();
        httpRequestLine.setUrl("/post/stub");
        requestData.setHttpRequestLine(httpRequestLine);
        requestData.getHttpRequestLine().setHttpMethod(HttpMethod.POST);

        // when
        Response response = dispatcher.handleRequest(requestData);

        // then
        assertThat(response.getHttpStatus()).isEqualTo(HttpStatus.FOUND);
    }

    @AfterEach
    void tearDown() {
        DataBase.deleteAll();
    }

    private static class GetStub extends HttpServlet {
        @Override
        public Response doGet(Request request, Response response) {
            Response result = new Response();
            result.setHttpStatus(HttpStatus.OK);
            return result;
        }
    }

    private static class PostStub extends HttpServlet {
        @Override
        public Response doPost(Request request, Response response) {
            Response result = new Response();
            result.setHttpStatus(HttpStatus.FOUND);
            return result;
        }
    }

}
