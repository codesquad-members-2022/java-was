package http;

import java.util.Arrays;
import java.util.Optional;

public enum HttpMethod {
    GET,
    POST
    ;

    public static Optional<HttpMethod> findMethod(String input) {
        return Arrays.stream(HttpMethod.values())
            .filter(method -> method.name().equals(input))
            .findAny();
    }
}
