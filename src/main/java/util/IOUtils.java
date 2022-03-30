package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        return String.copyValueOf(body);
    }

    public static List<Pair> readRequestHeader(BufferedReader br) throws IOException {

        List<Pair> headerPairs = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            if ("".equals(line)) {
                return headerPairs;
            }
            headerPairs.add(HttpRequestUtils.parseHeader(line));
        }

        return headerPairs;

    }

}
