package config;

import java.util.HashMap;
import java.util.Map;

import http.HttpMethod;
import http.HttpServlet;
import servlet.UserCreateServlet;

public class RequestMapping {
    private static final Map<String, MappingMethod> getMapping = new HashMap<>();

    private static final boolean NOT_CONTAINS = false;

    public static boolean contains(HttpMethod httpMethod, String url) {
        if (httpMethod.equals(HttpMethod.GET)) {
            return getMapping.containsKey(url);
        }
        return NOT_CONTAINS;
    }

}
