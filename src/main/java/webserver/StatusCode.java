package webserver;

public enum StatusCode {
	SUCCESSFUL_200("200", "OK"),
	REDIRECTION_302("302", "Found");

	private String code;
	private String message;

	StatusCode(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
