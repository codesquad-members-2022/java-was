package com.riakoader.was.util;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

public class HttpRequestUtils {

    /**
     * 간단하게 Handler 레이어는 depth 1, HandlerMethod 레이어는 depth 2 로 가정하여 처리해야할 uri 를 적절히 추출해낸다.
     *
     * @param requestURI
     * @param depth
     * @return
     */
    public static String getCurrentPath(String requestURI, int depth) {
        String[] path = requestURI.split("/");
        return "/" + (path.length == depth ? "" : path[depth]);
    }

    /**
     * @param queryString URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param cookies 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens)
                .map(t -> getKeyValue(t, "="))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }

    static Pair<String, String> getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair<>(tokens[0], tokens[1]);
    }

    public static Pair<String, String> parseHeader(String header) {
        return getKeyValue(header, ": ");
    }
}
