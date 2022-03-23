package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    public static String getRequestMethod(String requestLine) {
        return requestLine.split(" ")[0];
    }

    public static String getRequestURI(String requestLine) {
        return requestLine.split(" ")[1];
    }

    public static String getRequestPath(String requestURI) {
        return requestURI.split("\\?")[0];
    }

    public static String getQueryString(String requestURI) {
        return requestURI.split("\\?")[1];
    }

    /**
     * @param queryString 은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     *
     * @return key에 쿼리인자 이름, value에 쿼리값이 들어간 맵
     *
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&", "=");
    }

    /**
     * @param cookies 값은 name1=value1; name2=value2 형식임
     *
     * @return key에 쿠키 이름, value에 쿠키 값이 들어간 맵
     *
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";", "=");
    }

    public static Map<String, String> parseHeaders(String headers) {
        return parseValues(headers, "\n", ": ");
    }

    private static Map<String, String> parseValues(String values, String separator, String keyValueSeparator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
                .map(t -> getKeyValue(t, keyValueSeparator))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static class Pair {
        private final String key;
        private final String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;

            result = prime * result + key.hashCode();
            result = prime * result + value.hashCode();

            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;

            Pair other = (Pair) obj;

            return (key.equals(other.key) && value.equals(other.value));
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
