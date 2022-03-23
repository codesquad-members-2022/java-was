package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private Status status;
    private Map<String, String> header;
    private byte[] body;

    public Response(Status status, Map<String, String> header, byte[] body) {
        this.status = status;
        this.header = header;
        this.body = body;
    }

    public Response(Status status, Map<String, String> header) {
        this(status, header, null);
    }

    public Status getStatus() {
        return status;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
