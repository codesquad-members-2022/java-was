package model.request;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestBody {

    private final String body;

    public HttpRequestBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        this.body = getBody(bufferedReader, contentLength);
    }

    private String getBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        return readData(bufferedReader, contentLength);
    }

    private String readData(BufferedReader bufferedReader, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        bufferedReader.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
