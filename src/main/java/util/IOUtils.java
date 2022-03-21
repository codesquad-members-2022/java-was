package util;

import com.google.common.base.Strings;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import util.HttpRequestUtils.Pair;

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
        String headLine;
        List<Pair> headers = new ArrayList<>();
        while (!Strings.isNullOrEmpty(headLine = br.readLine())) {
            headers.add(HttpRequestUtils.parseHeader(headLine));
        }
        return headers;
    }

    public static byte[] readRequestResource(String url) throws IOException {
        return Files.readAllBytes(new File("./webapp" + url).toPath());
    }
}
