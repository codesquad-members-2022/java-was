package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class RequestReader {

    private final BufferedReader br;

    public RequestReader(InputStream in) {
        this.br = new BufferedReader(new InputStreamReader(in));
    }

    public Request create() throws IOException {
        return new Request(readRequestLine(), parseHeader());
    }

    private String readRequestLine() throws IOException {
        return br.readLine();
    }

    private Map<String, String> parseHeader() throws IOException {
        String line;
        Map<String, String> header = new HashMap<>();

        while (!(line = br.readLine()).equals("")) {
            String[] tokens = line.split(": ");

            if (tokens.length == 2) {
                header.put(tokens[0], tokens[1]);
            }
        }
        return header;
    }


}
