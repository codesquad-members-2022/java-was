package webserver;

public enum HttpMethod {

	GET, POST;

	public static HttpMethod create(String httpMethod) {
		if (httpMethod.equals("GET")) {
			return GET;
		}
		if (httpMethod.equals("POST")) {
			return POST;
		}
		return null;
	}

	public static boolean isGet(Request request) {
		return request.getHttpMethod().equals(GET);
	}

	public static boolean isPost(Request request) {
		return request.getHttpMethod().equals(POST);
	}

}
