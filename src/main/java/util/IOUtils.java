package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class IOUtils {

    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static void printRequestHeader(List<String> headerMessages) throws IOException {
        log.info("Request line: {}", headerMessages.get(0));
        for (int i = 1; i < headerMessages.size(); i++) {
            log.info("header: {}", headerMessages.get(i));
        }
    }


}
