package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestLineUtil;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private final List<String> headers = new ArrayList<>();

    public Request(BufferedReader bufferedReader) throws IOException {
        String line;
        while (!(line = bufferedReader.readLine()).equals("")) {
            if (line == null) {
                throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
            }
            // 로그 띄울 라인을 추가
            headers.add(line);
        }
    }

    public String getURL() {
        return RequestLineUtil.getURL(headers.get(0));
    }

    public void outputLog() {
        headers.stream().forEach(s -> log.debug("Request: {}", s));
    }
}
