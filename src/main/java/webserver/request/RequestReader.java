package webserver.request;

import com.google.common.base.Strings;
import util.HttpRequestUtils;
import util.IOUtils;
import webserver.request.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {

    private final BufferedReader bufferedReader;

    public RequestReader(InputStream inputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    public Request create() throws IOException {

        String requestLine = HttpRequestUtils.UrlDecode(bufferedReader.readLine());
        Map<String, String> headers = createHeader();
        String requestBody = createRequestBody(headers);

        return new Request(requestLine, headers, requestBody);
    }

    private Map<String, String> createHeader() throws IOException {
        String headerLine = bufferedReader.readLine();
        Map<String, String> headers = new HashMap<>();
        while(!Strings.isNullOrEmpty(headerLine)) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(headerLine);
            headers.put(pair.getKey(), pair.getValue());
            headerLine = bufferedReader.readLine();
        }
        return headers;
    }

    private String createRequestBody(Map<String, String> headers) throws IOException {
        if (headers.get("Content-Length") == null) {
            return "";
        }

        int contentLength = Integer.parseInt(headers.get("Content-Length"));

        return IOUtils.readData(bufferedReader, contentLength);
    }
}
