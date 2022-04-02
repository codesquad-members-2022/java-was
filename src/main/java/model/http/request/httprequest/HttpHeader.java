package model.http.request.httprequest;

import configuration.HttpHeaders;
import configuration.ObjectFactory;
import model.http.HeaderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Objects.isNull;
import static util.SpecialCharacters.NULL_STRING;

public class HttpHeader {

    private static final String REQUESTLINE_DELIMETER = ": ";
    private static final int HEADER_TYPE = 0;
    private static final int HEADER_VALUE = 1;
    private static final String CONTENT_LENGTH = "Content-Length";

    private Map<String, String> headers;

    private static final HttpHeaders httpHeaderMap = ObjectFactory.httpHeaders;

    public HttpHeader(BufferedReader bufferedReader) throws IOException {
        this.headers = getHttpHeader(bufferedReader);
    }

    public String getHeaderType(String type) {
        return httpHeaderMap.getHeaderType(type);
    }

    public Map<String, String> getHeaders() {
        return new HashMap<>(headers);
    }

    private Map<String, String> getHttpHeader(BufferedReader bufferedReader) throws IOException {
        Map<String, String> header = new HashMap<>();
        String line;
        while (!(line = bufferedReader.readLine()).equals(NULL_STRING)) {
            addHeader(header, line);
        }
        return header;
    }

    private void addHeader(Map<String, String> headers, String line) {
        String[] keyAndValue = line.split(REQUESTLINE_DELIMETER);
        String type = keyAndValue[HEADER_TYPE];
        String value = keyAndValue[HEADER_VALUE];
        headers.put(type, value);
    }

    public int getContentLength() {
        return Objects.nonNull(headers.get(CONTENT_LENGTH)) ? Integer.parseInt(headers.get(CONTENT_LENGTH)) : 0;
    }
}
