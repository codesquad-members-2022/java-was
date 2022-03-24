package config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import http.HttpMethod;
import http.HttpServlet;
import servlet.UserCreateServlet;

public class RequestMapping {
    private static final Map<URL, Class<? extends HttpServlet>> servletMapping = new HashMap<>();

    private static final boolean NOT_CONTAINS = false;

    static {
        servletMapping.put(new URL("/user/create"), UserCreateServlet.class);
    }

    public static boolean contains(HttpMethod httpMethod, String url) {
        if (httpMethod.equals(HttpMethod.GET)) {
            return servletMapping.containsKey(new URL(url));
        }
        return NOT_CONTAINS;
    }

    public static Optional<Class<? extends HttpServlet>> findHandlerMethod(String url) {
        return Optional.ofNullable(servletMapping.get(new URL(url)));
    }

    private static class URL {
        private final String url;

        private URL(String url) {
            this.url = url;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            URL url1 = (URL)o;
            return Objects.equals(url, url1.url);
        }

        @Override
        public int hashCode() {
            return Objects.hash(url);
        }
    }
}
