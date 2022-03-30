package webserver;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Response {

	private static final String HTTP_VERSION = "HTTP/1.1";

	private StatusCode statusCode;
	private final Map<String, Object> responseHeaderMap = new LinkedHashMap<>();
	private String body;

	public Response() {
		statusCode = StatusCode.SUCCESSFUL_200;
	}

	public void setStatus(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public void setContentType(String type) {
		responseHeaderMap.put("Content-Type", type);
	}

	public void setRedirect(StatusCode redirectionStatusCode, String redirectionPath) {
		statusCode = redirectionStatusCode;
		responseHeaderMap.put("Location", redirectionPath);
	}

	public void setBody(byte[] responseBody, String contentType) {
		body = new String(responseBody, StandardCharsets.UTF_8);
		responseHeaderMap.put("Content-Length", responseBody.length);

		contentType = parseContentType(contentType);
		responseHeaderMap.put("Content-Type", contentType);
	}

	private String parseContentType(String contentType) {
		if (contentType.equals("html")) {
			return "text/html";
		} else if (contentType.equals("js")) {
			return "application/js";
		} else if (contentType.equals("css")) {
			return "text/css";
		} else {
			return "";
		}
	}

	public void setBody(String responseBody, String contentType) {
		body = responseBody;
		responseHeaderMap.put("Content-Length", responseBody.length());
		responseHeaderMap.put("Content-Type", contentType);
	}

	public String toHeader() {
		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(HTTP_VERSION).append(" ")
			.append(statusCode.getCode()).append(" ")
			.append(statusCode.getMessage()).append(System.lineSeparator());

		for (Entry<String, Object> entry : responseHeaderMap.entrySet()) {
			String headerKey = entry.getKey();
			Object headerValue = entry.getValue();

			stringBuffer.append(headerKey).append(": ")
				.append(headerValue).append(System.lineSeparator());
		}
		stringBuffer.append(System.lineSeparator());

		return stringBuffer.toString();
	}

	public String toBody() {
		return body + "\r\n";
	}

	public void setCookie(String cookie) {
		responseHeaderMap.put("Set-Cookie", "sessionId=" + cookie + "; Path=/");
	}

	public void setDeleteCookie() {
		responseHeaderMap.put("Set-Cookie", "sessionId=deleted; " +
			"path=/; Max-Age=0; expires=Thu, 01 Jan 1970 00:00:00 GMT/");
	}
}
