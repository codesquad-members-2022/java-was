package com.riakoader.was.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.riakoader.was.httpmessage.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtils.class);

    /**
     * 클라이언트가 보낸 데이터 스트림을 'RequestLine', 'RequestHeaders', (+ RequestMessageBody) 로 구분 지어 읽어들인다.
     * 읽어들인 메시지들을 사용하여 HttpRequest 객체를 생성하고 이를 반환한다.
     *
     * @param in
     * @return 'InputStream' 에서 읽어온 데이터로 HttpRequest 객체를 생성하여 반환한다.
     * @throws IOException
     */
    public static HttpRequest receiveRequest(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String requestLine = line;

        logger.debug("request line : {}", line);

        Map<String, String> headers = new HashMap<>();

        while (true) {
            line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);

            if (Strings.isNullOrEmpty(line)) {
                break;
            }

            Pair<String, String> pair = HttpRequestUtils.parseHeader(line);
            headers.put(pair.getKey(), pair.getValue());

            logger.debug("header : {}", line);
        }

        String requestMessageBody = URLDecoder.decode(IOUtils.readData(br, getContentLength(headers)), StandardCharsets.UTF_8);

        return new HttpRequest(requestLine, headers, requestMessageBody);
    }

    private static int getContentLength(Map<String, String> headers) {
        return Integer.parseInt(Optional.ofNullable(headers.get("Content-Length")).orElse(String.valueOf(0)));
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
                .filter(p -> p != null)
                .collect(Collectors.toMap(p -> (String) p.getKey(), p -> (String) p.getValue()));
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
