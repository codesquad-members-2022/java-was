package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.RequestLineUtil;

public class RequestReader {

    private static final Logger log = LoggerFactory.getLogger(RequestReader.class);

    private final BufferedReader bufferedReader;

    public RequestReader(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public Request getRequest() throws IOException {
        RequestLine requestLine = makeRequestLine(bufferedReader);

        Map<String, String> headers = makeRequestHeaders(bufferedReader);
        outputLog(headers);

        String messageBody = "";
        if (requestLine.isPostMethodType()) {
            messageBody = IOUtils.readData(bufferedReader,
                Integer.parseInt(headers.get("Content-Length:")));
            log.debug(messageBody);
        }

        return new Request(requestLine, headers, messageBody);
    }

    private void outputLog(Map<String, String> headers) {
        // Request Headers Log
        for (Entry<String, String> entry : headers.entrySet()) {
            log.debug("Request: {} {}", entry.getKey(), entry.getValue());
        }
    }

    private RequestLine makeRequestLine(BufferedReader bufferedReader) throws IOException {
        String line;
        if ("".equals(line = bufferedReader.readLine()) || line == null) {
            throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
        }
        // Request Line Log
        log.debug(line);

        String url = RequestLineUtil.getURL(line);
        String methodType = RequestLineUtil.getMethodType(line);
        return new RequestLine(methodType, url);
    }

    private Map<String, String> makeRequestHeaders(BufferedReader bufferedReader)
        throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!"".equals(line = bufferedReader.readLine())) {
            if (line == null) {
                throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
            }
            String[] splitLine = line.split(" ");
            headers.put(splitLine[0], splitLine[1]);
        }
        return headers;
    }
}
