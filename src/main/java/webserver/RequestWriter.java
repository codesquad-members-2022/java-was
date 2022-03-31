package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class RequestWriter {
    private static final Logger log = LoggerFactory.getLogger(RequestWriter.class);
    private List<String> messages;

    public RequestWriter() {
        messages = new ArrayList<>();
    }

    public static Request from(BufferedReader bufferedReader) {
        RequestWriter requestWriter = new RequestWriter();
        try {
            String line = bufferedReader.readLine();
            requestWriter.messages.add(line);
            log.debug(line);
            while (!line.equals("")) {
                line = bufferedReader.readLine();
                requestWriter.messages.add(line);
            }
            Request request = new Request(requestWriter.messages);
            request.of(bufferedReader);
            return request;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
