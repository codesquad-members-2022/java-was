package webserver.request;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import webserver.Request;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Request 클래스")
class RequestTest {

	@Nested
	@DisplayName("생성자는(new Request(String requestLine, List<String> rawData")
	class Describe_constructor {
	    @Nested
	    @DisplayName("만약 간단한 GET 요청이라면")
	    class Context_with_normal_GET_Request_Message {

			@Test
	        @DisplayName("Request 객체를 생성한다.")
	        void It_returns_object_request() {
				List<String> rawData = new ArrayList<>();
				String requestLine = "GET /user/create HTTP/1.1";
				rawData.add("Host: localhost:8080");
				rawData.add("Connection: keep-alive");
				rawData.add("Accept: */*");
 				String rawBody = "";

				Request request = new Request(requestLine, rawData, rawBody);
				Map<String, String> headerMap = request.getRequestHeaderMap();

				assertThat(request.getMethod()).isEqualTo("GET");
				assertThat(request.getUri()).isEqualTo("/user/create");
				assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
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
				String requestLine = "GET /user/create?username=kukim&password=1234 HTTP/1.1";
				rawData.add("Host: localhost:8080");
				rawData.add("Connection: keep-alive");
				rawData.add("Accept: */*");
				String rawBody = "";

				Request request = new Request(requestLine, rawData, rawBody);
				Map<String, String> headerMap = request.getRequestHeaderMap();

				assertThat(request.getMethod()).isEqualTo("GET");
				assertThat(request.getUri()).isEqualTo("/user/create");
				assertThat(request.getVersion()).isEqualTo("HTTP/1.1");
				assertThat(request.getParam("username")).isEqualTo("kukim");
				assertThat(request.getParam("password")).isEqualTo("1234");
				assertThat(headerMap.get("Host")).isEqualTo("localhost:8080");
				assertThat(headerMap.get("Connection")).isEqualTo("keep-alive");
				assertThat(headerMap.get("Accept")).isEqualTo("*/*");

			}
		}
	}

}
