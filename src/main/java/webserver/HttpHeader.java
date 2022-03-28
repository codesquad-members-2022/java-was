package webserver;

import model.HeaderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static util.SpecialCharacters.NULL_STRING;

public class HttpHeader {

    private static final String COLON = ": ";
    private static final int HEADER_TYPE = 0;
    private static final int HEADER_VALUE = 1;

    private RequestLine requestLine;
    private Map<HeaderType, String> headers;

    private static final HttpHeaders httpHeaderMap = new HttpHeaders();

    public HttpHeader(BufferedReader bufferedReader) throws IOException {
        this.headers = getHttpHeader(bufferedReader);
    }

    public Map<HeaderType, String> getHeaders() {
        return new HashMap<>(headers);
    }

    private Map<HeaderType, String> getHttpHeader(BufferedReader bufferedReader) throws IOException {
        Map<HeaderType, String> header = new ConcurrentHashMap<>();
        String line;
        while (!(line = bufferedReader.readLine()).equals(NULL_STRING)) {
            addHeader(header, line);
        }
        return header;
    }

    private void addHeader(Map<HeaderType, String> headers, String line) {
        String[] keyAndValue = line.split(COLON);
        String type = keyAndValue[HEADER_TYPE];
        String value = keyAndValue[HEADER_VALUE];
        headers.put(getHeaderType(type), value);
    }

    private HeaderType getHeaderType(String type) {
        return httpHeaderMap.getHeaderType(type);
    }

}
