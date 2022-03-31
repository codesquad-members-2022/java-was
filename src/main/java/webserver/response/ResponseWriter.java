package webserver.response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Optional;


public class ResponseWriter {

    private static final String NEW_LINE = "\r\n";
    private static final String BLANK = " ";
    private static final String HEADER_DELIMETER = ": ";

    private final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

    private final Response response;
    private final DataOutputStream dos;

    public ResponseWriter(Response response, OutputStream outputStream) {
        this.response = response;
        this.dos = new DataOutputStream(outputStream);
    }

    public void writeResponse() {
       try {
           writeStartLine();
           writeHeaders();
           writeBody();
           dos.flush();
       } catch (IOException e) {
           e.printStackTrace();
       }
    }

    private void writeStartLine() throws IOException {
        dos.writeBytes(response.getProtocol() + BLANK + response.getStatus() + BLANK + NEW_LINE);
    }

    private void writeHeaders() throws IOException {
        for (Map.Entry<String, String> entry : response.getHeaders().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            dos.writeBytes(key + HEADER_DELIMETER + value + NEW_LINE);
        }
        dos.writeBytes(NEW_LINE);
    }

    private void writeBody() throws IOException {
        Optional<byte[]> bodyOptional = response.getBody();
        if (bodyOptional.isPresent()) {
            byte[] body = bodyOptional.get();
            dos.write(body, 0, body.length);
        }
    }
}
