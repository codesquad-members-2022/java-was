package webserver.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import webserver.Response;
import webserver.StatusCode;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Response 클래스는")
class ResponseTest {

	@Test
	@DisplayName("Reponse 객체, 헤더 상태코드 200, Content-Type: text/html 인 경우")
	void response_객체생성_200() {
		Response response = new Response();
		response.setStatus(StatusCode.SUCCESSFUL_200);
		response.setContentType("text/html");

		String result = response.toHeader();

		assertThat(result).isEqualTo("HTTP/1.1 200 OK\r\n" +
			"Content-Type: text/html\r\n" +
			"\r\n");
	}

	@Test
	@DisplayName("Reponse 객체, 헤더 상태코드 302, Locaion: http://localhost:8080/index.html인 경우")
	void response_객체생성_302() {
		Response response = new Response();
		response.setRedirect(StatusCode.REDIRECTION_302, "http://localhost:8080/index.html");

		String result = response.toHeader();

		assertThat(result).isEqualTo("HTTP/1.1 302 Found\r\n" +
			"Location: http://localhost:8080/index.html\r\n" +
			"\r\n");
	}

	@Test
	@DisplayName("Reponse 객체, 헤더 상태코드 200, 간단한 바디 있는 경우")
	void response_객체생성_바디포함() {
		Response response = new Response();
		response.setStatus(StatusCode.SUCCESSFUL_200);
		response.setContentType("text/html");
		String responseBody = "<html></html>";
		response.setBody(responseBody, "text/html");

		String resultHeader = response.toHeader();
		String resultBody = response.toBody();

		assertThat(resultHeader).isEqualTo("HTTP/1.1 200 OK\r\n" +
			"Content-Type: text/html\r\n" +
			"Content-Length: 13\r\n" +
			"\r\n");
		assertThat(resultBody).isEqualTo("<html></html>");
	}
}
