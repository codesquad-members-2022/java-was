package util;

import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.System.out;

public class ResponseBuilder {
    private static final Logger log = LoggerFactory.getLogger(ResponseBuilder.class);

    private final HttpResponse response;
    private final DataOutputStream dos;

    public ResponseBuilder(HttpResponse response, OutputStream out) {
        this.response = response;
        this.dos = new DataOutputStream(out);
    }

    public void sendResponse() {
        try {
            writeStatusLine(response.getStatusLine());
            writeHeader(response.getHeader());
            writeBody(response.getBody());
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeStatusLine(String response) throws IOException {
        dos.writeBytes(response + "\r\n");
    }

    private void writeHeader(Map<String, String> header) throws IOException {
        List<String> keySet = new ArrayList<>(header.keySet());
        for (int i = 0; i < header.size(); i++) {
            String key = keySet.get(i);
            dos.writeBytes(key + ": " + header.get(key) + "\r\n");
        }
        dos.writeBytes("\r\n");
    }

    private void writeBody(byte[] body) throws IOException {
        if (body == null) {
            return;
        }
        dos.write(body, 0, body.length);
    }
}
