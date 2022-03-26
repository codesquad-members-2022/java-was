package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {

    private final BufferedReader bufferedReader;

    public RequestReader(InputStream in) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(in));
    }

    public Request create() throws IOException {
        return new Request(bufferedReader.readLine(), parseHeader());
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
}
