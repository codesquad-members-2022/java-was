package webserver;

import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {

    private final BufferedReader bufferedReader;
    private static final String CONTENT_LENGTH = "Content-Length";

    public RequestReader(InputStream in) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(in));
    }

    public Request create() throws IOException {
        String line = bufferedReader.readLine();
        Map<String, String> header = parseHeader();

        if (header.get(CONTENT_LENGTH) != null) {
            Map<String, String> body = parseBody(header.get(CONTENT_LENGTH));
            return new Request(line, header, body);
        }
        return new Request(line, header);
    }

    private Map<String, String> parseHeader() throws IOException {
        String line;
        Map<String, String> headers = new HashMap<>();

        while (!(line = bufferedReader.readLine()).equals("")) {
            String[] tokens = line.split(": ");

            if (tokens.length == 2) {
                headers.put(tokens[0], tokens[1]);
            }
        }
        return headers;
    }

    private Map<String, String> parseBody(String contentLength) throws IOException {
        int length = Integer.parseInt(contentLength);
        String body = IOUtils.readData(bufferedReader, length);
        return HttpRequestUtils.parseQueryString(body);
    }
}
