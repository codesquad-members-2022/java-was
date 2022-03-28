package webserver.request;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.Request;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Request 클래스")
class RequestTest {

	@Nested
	@DisplayName("생성자는(new Request(List<String> rawHeader, String rawBody))")
	class Describe_constructor {
	    @Nested
	    @DisplayName("만약 간단한 GET 요청이라면")
	    class Context_with_normal_GET_Request_Message {

			@Test
	        @DisplayName("Request 객체를 생성한다.")
	        void It_returns_object_request() {
				List<String> rawData = new ArrayList<>();
				rawData.add("GET /user/create HTTP/1.1");
				rawData.add("Host: localhost:8080");
				rawData.add("Connection: keep-alive");
				rawData.add("Accept: */*");
 				String rawBody = "";

				Request result = new Request(rawData, rawBody);
				Map<String, String> headerMap = result.getRequestHeaderMap();

				assertThat(result.getMethod()).isEqualTo("GET");
				assertThat(result.getUri()).isEqualTo("/user/create");
				assertThat(result.getVersion()).isEqualTo("HTTP/1.1");
				assertThat(headerMap.get("Host")).isEqualTo("localhost:8080");
				assertThat(headerMap.get("Connection")).isEqualTo("keep-alive");
				assertThat(headerMap.get("Accept")).isEqualTo("*/*");
			}
	    }

		@Nested
		@DisplayName("만약 쿼라파라미터가있는 GET 요청이라면")
		class Context_with_queryString_GET_Request_Message {

			@Test
			@DisplayName("Request 객체를 생성한다.")
			void It_returns_object_request() {
				List<String> rawData = new ArrayList<>();
				rawData.add("GET /user/create?username=kukim&password=1234 HTTP/1.1");
				rawData.add("Host: localhost:8080");
				rawData.add("Connection: keep-alive");
				rawData.add("Accept: */*");
				String rawBody = "";

				Request result = new Request(rawData, rawBody);
				Map<String, String> headerMap = result.getRequestHeaderMap();

				assertThat(result.getMethod()).isEqualTo("GET");
				assertThat(result.getUri()).isEqualTo("/user/create");
				assertThat(result.getVersion()).isEqualTo("HTTP/1.1");
				assertThat(result.getParam("username")).isEqualTo("kukim");
				assertThat(result.getParam("password")).isEqualTo("1234");
				assertThat(headerMap.get("Host")).isEqualTo("localhost:8080");
				assertThat(headerMap.get("Connection")).isEqualTo("keep-alive");
				assertThat(headerMap.get("Accept")).isEqualTo("*/*");

			}
		}

		@Nested
		@DisplayName("만약 POST /user/create 요청이라면")
		class Context_with_POST_user_create_Request_Message {

			@Test
			@DisplayName("Request 객체를 생성한다.")
			void It_returns_object_request() {
				List<String> rawData = new ArrayList<>();
				rawData.add("POST /user/create HTTP/1.1");
				rawData.add("Host: localhost:8080");
				rawData.add("Connection: keep-alive");
				rawData.add("Content-Type: application/x-www-form-urlencoded");
				rawData.add("Accept: */*");
				String rawBody = "userId=test&password=1234&name=test&email=test%40com";

				Request result = new Request(rawData, rawBody);

				assertThat(result.getParam("userId")).isEqualTo("test");
				assertThat(result.getParam("password")).isEqualTo("1234");
				assertThat(result.getParam("name")).isEqualTo("test");
				assertThat(result.getParam("email")).isEqualTo("test@com");
			}
		}
	}

	@Test
	void 확장자테스트_있을때() {
		List<String> rawData = new ArrayList<>();
		rawData.add("GET /index.html HTTP/1.1");
		rawData.add("Host: localhost:8080");
		rawData.add("Connection: keep-alive");
		rawData.add("Accept: */*");
		String rawBody = "";

		Request result = new Request(rawData, rawBody);


		String fileExtention = result.getFileExtension();
		assertThat(fileExtention).isEqualTo("html");
	}

	@Test
	void 확장자테스트_없을때() {
		List<String> rawData = new ArrayList<>();
		rawData.add("GET /user/create HTTP/1.1");
		rawData.add("Host: localhost:8080");
		rawData.add("Connection: keep-alive");
		rawData.add("Accept: */*");
		String rawBody = "";

		Request result = new Request(rawData, rawBody);

		String fileExtention = result.getFileExtension();
		assertThat(fileExtention).isEqualTo("");
	}
}
