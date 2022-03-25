package util;

import util.HttpRequestUtils.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {

    public static final String STATIC_RESOURCE_PATH = "./webapp";

    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return URLDecoder.decode(String.copyValueOf(body), StandardCharsets.UTF_8);
    }

    public static List<Pair> readRequestHeader(BufferedReader br) throws IOException {
        String headLine;
        List<Pair> headers = new ArrayList<>();
        while (!(headLine = br.readLine()).equals("") && headLine != null) {
            headers.add(HttpRequestUtils.parseHeader(headLine));
        }
        return headers;
    }

    public static byte[] readRequestResource(String url) throws IOException {
        if (url.equals("/")) {
            url = "/index.html";
        }
        return Files.readAllBytes(new File(STATIC_RESOURCE_PATH + url).toPath());
    }
}
