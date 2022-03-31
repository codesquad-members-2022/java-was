package webserver;

import java.util.Objects;

public class ControllerMapper {

	private final HttpMethod httpMethod;
	private final String path;

	public ControllerMapper(HttpMethod httpMethod, String path) {
		this.httpMethod = httpMethod;
		this.path = path;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ControllerMapper that = (ControllerMapper) o;
		return httpMethod == that.httpMethod && Objects.equals(path, that.path);
	}

	@Override
	public int hashCode() {
		return Objects.hash(httpMethod, path);
	}

}
