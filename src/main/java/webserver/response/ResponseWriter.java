package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;


public class ResponseWriter {

    private static final String NEW_LINE = "\r\n";
    private static final String BLANK = " ";
    private static final String CONTENT_TYPE_KEY = "Content-Type: ";
    private static final String CHARSET = "charset=utf-8";
    private static final String CONTENT_LENGTH_KEY = "Content-Length: ";
    private static final String LOCATION_KEY = "Location: ";
    private static final String ROOT_PATH = "./webapp";

    private final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    private final Response response;
    private final DataOutputStream dos;

    public ResponseWriter(Response response, OutputStream outputStream) {
        this.response = response;
        this.dos = new DataOutputStream(outputStream);
    }

    public void writeResponse() {
        try {
            dos.writeBytes(response.getProtocol() + BLANK + response.getStatus() + NEW_LINE);
            if (response.getContentType() == null) {
                write302();
            }
            if (response.getLocation() == null) {
                write200();
            }
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }

    }

    private void write200() throws IOException {
        dos.writeBytes(CONTENT_TYPE_KEY + response.getContentTypeAsString() + CHARSET + NEW_LINE);
        byte[] body = parseResource();
        dos.writeBytes(CONTENT_LENGTH_KEY + body.length + NEW_LINE);
        dos.writeBytes(NEW_LINE);
        dos.write(body, 0, body.length);
    }

    private byte[] parseResource() throws IOException {
        return Files.readAllBytes(new File(ROOT_PATH + response.getViewPath()).toPath());
    }

    private void write302() throws IOException {
        if(response.getSession() != null) {
            if(response.getSession().equals("deleted")) {
                dos.writeBytes("Set-Cookie: sessionId=" + response.getSession() +"; Max-Age=0; path=/;" + NEW_LINE);
            } else {
                dos.writeBytes("Set-Cookie: sessionId=" + response.getSession() +"; path=/;" + NEW_LINE);
            }
        }
        dos.writeBytes(LOCATION_KEY + response.getLocation() + NEW_LINE);
    }


}
