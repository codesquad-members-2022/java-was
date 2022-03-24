package util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import util.HttpRequestUtils.Pair;

class RequestTest {

	@Test
	@DisplayName("QueryString이 없는 RequestLine이 요청되면 Path를 반환한다.")
	void takePath() {
		//given
		Request request = new Request("GET /index.html HTTP/1.1");

		//when
		String result = request.takePath();

		//then
		assertThat("/index.html").isEqualTo(result);
	}

	@Test
	@DisplayName("QueryString이 있는 RequestLine이 요청되면 Path를 반환한다.")
	void takePathWithQuery() {
		//given
		Request request = new Request("GET /user/create?userId=jeremy0405&password=qwer&name=%EC%9E%A5+%ED%98%95+%EC%84%9D&email=jeremy0405%40naver.com HTTP/1.1");

		//when
		String result = request.takePath();

		//then
		assertThat("/user/create").isEqualTo(result);
	}

	@Test
	@DisplayName("QueryString이 있는 RequestLine이 요청되면 QueryString을 추출해서 Map으로 반환한다.")
	void takeParsedQueryString() {
		//given
		Request request = new Request("GET /user/create?userId=jeremy0405&password=qwer&name=%EC%9E%A5+%ED%98%95+%EC%84%9D&email=jeremy0405%40naver.com HTTP/1.1");

		//when
		Map<String, String> parsedQueryString = request.takeParsedQueryString();

		//then
		assertThat("jeremy0405").isEqualTo(parsedQueryString.get("userId"));
		assertThat("qwer").isEqualTo(parsedQueryString.get("password"));
		assertThat("장 형 석").isEqualTo(parsedQueryString.get("name"));
		assertThat("jeremy0405@naver.com").isEqualTo(parsedQueryString.get("email"));
	}

	@Test
	@DisplayName("QueryString이 없는 RequestLine이 요청되면 value가 null인 Map으로 반환한다.")
	void takeNullQueryString() {
		//given
		Request request = new Request("GET /index.html HTTP/1.1");

		//when
		Map<String, String> parsedQueryString = request.takeParsedQueryString();

		//then
		assertThat(parsedQueryString.get("userId")).isNull();
		assertThat(parsedQueryString.get("password")).isNull();
		assertThat(parsedQueryString.get("name")).isNull();
		assertThat(parsedQueryString.get("email")).isNull();
	}

	@Test
	@DisplayName("클라이언트 요청 시 Request Header를 추출해서 List<Pair>로 반환한다.")
	void takeHeaderPairs() throws IOException {
		//given
		Request request = new Request("GET /index.html HTTP/1.1");

		String requestHeader = "Host: localhost:8080\r\n"
			+ "User-Agent: curl/7.79.1\r\n"
			+ "Accept: */*\r\n"
			+ "\r\n";
		InputStream is = new ByteArrayInputStream(requestHeader.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		//when
		List<Pair> headerPairs = request.takeHeaderPairs(br);

		//then
		assertThat(headerPairs).hasSize(3);
		assertThat(new Pair("Host", "localhost:8080")).isEqualTo(headerPairs.get(0));
		assertThat(new Pair("User-Agent", "curl/7.79.1")).isEqualTo(headerPairs.get(1));
		assertThat(new Pair("Accept", "*/*")).isEqualTo(headerPairs.get(2));
	}

}
