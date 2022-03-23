package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.Pair;
import static util.HttpRequestUtils.parseHeader;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return URLDecoder.decode(String.valueOf(body), StandardCharsets.UTF_8);
    }

    public static String readRequestLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    public static Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> requestHeader = new HashMap<>();
        String line;

        while ((line = br.readLine()) != null) {
            if (line.equals("")) {
                return requestHeader;
            }

            Pair field = parseHeader(line);
            requestHeader.put(field.getKey(), field.getValue());
        }
        return requestHeader;
    }
}
