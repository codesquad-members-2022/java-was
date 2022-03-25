package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.RequestLineUtil;

public class Request {

    private static final Logger log = LoggerFactory.getLogger(Request.class);

    private String requestLine;
    private String messageBody;
    private URL url;
    private final Map<String, String> headers = new HashMap<>();

    public Request(BufferedReader bufferedReader) throws IOException {
        String line;
        initRequestLine(bufferedReader);
        initURL();

        while (!("").equals(line = bufferedReader.readLine())) {
            if (line == null) {
                throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
            }
            String[] temp = line.split(" ");
            headers.put(temp[0], temp[1]);
        }
        outputLog();

        if (checkPostMethod()) {
            messageBody = IOUtils.readData(bufferedReader,
                Integer.parseInt(headers.get("Content-Length:")));
            log.debug(messageBody);
        }
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public URL getURL() {
        return url;
    }

    private void outputLog() {
        // Request Line Log
        log.debug(requestLine);
        // Request Headers Log
        for (Entry<String, String> entry : headers.entrySet()) {
            log.debug("Request: {} {}", entry.getKey(), entry.getValue());
        }
    }

    private void initRequestLine(BufferedReader bufferedReader) throws IOException {
        String line;
        if (("").equals(line = bufferedReader.readLine()) || line == null) {
            throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
        }
        this.requestLine = line;
    }

    private void initURL() {
        String decodedUrl = URLDecoder.decode(requestLine, StandardCharsets.UTF_8);
        url = new URL(RequestLineUtil.getURL(decodedUrl));
    }

    public boolean checkPostMethod() {
        return RequestLineUtil.getMethodType(requestLine).equals("POST");
    }
}

