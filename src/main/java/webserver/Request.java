package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {

    private String method;
    private String url;
    private String protocol;

    private Map<String, String> header = new HashMap<>();

    public Request(BufferedReader bufferedReader) throws IOException {
        String line = bufferedReader.readLine();
        parseLine(line);
        parseHeader(bufferedReader);
    }

    private void parseLine(String line) {
        String[] tokens = line.split(" ");
        method = tokens[0];
        url = tokens[1];
        protocol = tokens[2];
    }

    private void parseHeader(BufferedReader bufferedReader) throws IOException {
        String line;

        while (!(line = bufferedReader.readLine()).equals("")) {
            String[] tokens = line.split(": ");

            if (tokens.length == 2) {
                header.put(tokens[0], tokens[1]);
            }
        }
    }
}
