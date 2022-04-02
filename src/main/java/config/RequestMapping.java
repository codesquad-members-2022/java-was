package config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import http.HttpServlet;

public class RequestMapping {
    private final Map<URL, HttpServlet> servletMapping;

    private RequestMapping(Map<URL, HttpServlet> servletMapping) {
        this.servletMapping = servletMapping;
    }

    public boolean contains(String url) {
        return servletMapping.containsKey(new URL(url));
    }

    public Optional<HttpServlet> findHandlerMethod(String url) {
        return Optional.ofNullable(servletMapping.get(new URL(url)));
    }

    public static class Builder {
        private final Map<URL, HttpServlet> builderServletMapping = new HashMap<>();

        public Builder addMapping(String url, HttpServlet servlet) {
            builderServletMapping.put(new URL(url), servlet);
            return this;
        }

        public RequestMapping build() {
            return new RequestMapping(builderServletMapping);
        }
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
