package webserver.request;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import webserver.Request;

import static org.assertj.core.api.Assertions.*;

class RequestTest {

	@Test
	void request_생성() {
		List<String> rawData = new ArrayList<>();
		String firstLine = "GET /user/create?userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net HTTP/1.1";
		rawData.add("Host: localhost:8080");
		rawData.add("Connection: keep-alive");
		rawData.add("Accept: */*");

		Request request = new Request(firstLine, rawData);
		Map<String, String> headerMap = request.getRequestHeaderMap();

		assertThat(headerMap.get("Host")).isEqualTo("localhost:8080");
		assertThat(headerMap.get("Connection")).isEqualTo("keep-alive");
		assertThat(headerMap.get("Accept")).isEqualTo("*/*");
	}
}
