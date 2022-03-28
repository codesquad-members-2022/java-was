package webserver;

import java.util.HashMap;
import java.util.Map;

public enum HttpMethod {

    GET,
    POST;

    private static final Map<String, HttpMethod> mappings = new HashMap<>(2);

    static {
        for (HttpMethod httpMethod : values()) {
            System.out.println(httpMethod.name());
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    public static HttpMethod getMethod(String methodType) {
        return (methodType != null ? mappings.get(methodType) : null);
    }
}
