package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

public class ResponseWriter {

    private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    private DataOutputStream dos;

    public ResponseWriter(DataOutputStream dos) {
        this.dos = dos;
    }

    public void from(Response response) {
        try {
            recordLine(response.getStatus());
            recordHeader(response.getHeader());
            recordBody(response.getBody());

            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void recordLine(Status status) {
        try {
            dos.writeBytes("HTTP/1.1 " + status.getCode() + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void recordHeader(Map<String, String> header) {
        try {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
            }
            dos.writeBytes("\r\n");

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void recordBody(byte[] body) {
        if (body == null) {
            return;
        }

        try {
            dos.write(body, 0, body.length);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
