package webserver.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.HttpResponse;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class MyView {

    private static final Logger log = LoggerFactory.getLogger(MyView.class);

    private MyView() {}

    public static void render(DataOutputStream dos, HttpResponse response) {
        if (response != null) {
            writeResponse(dos, response);
        }
    }

    private static void writeResponse(DataOutputStream dos, HttpResponse response) {
        writeResponseLine(dos,response);
        writeResponseHeaders(dos, response);
        writeResponseBody(dos, response.getResponseBody());
    }

    private static void writeResponseLine(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(String.format("%s %d %s %s",
                    response.getVersion(), response.getHttpStatusCode(), response.getHttpStatusMessage(), System.lineSeparator()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void writeResponseHeaders(DataOutputStream dos, HttpResponse response) {
        try {
            Map<String, String> responseHeaders = response.getResponseHeaders();
            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                dos.writeBytes(String.format("%s: %s %s", entry.getKey(), entry.getValue(), System.lineSeparator()));
            }
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private static void writeResponseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
