package webserver;

public enum HttpStatus {

	OK(200, "200 OK"),
	FOUND(302, "302 Found");

	private int status;
	private String message;

	HttpStatus(int status, String message) {
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}
}
