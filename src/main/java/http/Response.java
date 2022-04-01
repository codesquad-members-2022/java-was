package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private final String httpVersion;
    private final String statusCode;
    private final String reasonPhrase;
    private final Map<String, String> responseHeaderField;
    private byte[] responseBody;

    public Response(String httpVersion, String statusCode, String reasonPhrase, Map<String, String> responseHeaderField, byte[] responseBody) {
        this.httpVersion = httpVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.responseHeaderField = responseHeaderField;
        this.responseBody = responseBody;
    }

    public String responseMessage() {
        StringBuilder responseMessage = new StringBuilder();
        appendStatusLine(responseMessage);
        appendResponseHeaderField(responseMessage);
        responseMessage.append("\r\n");
        responseMessage.append("\r\n");
        responseMessage.append(new String(responseBody));
        return responseMessage.toString();
    }

    private void appendStatusLine(StringBuilder responseMessage) {
        responseMessage.append(httpVersion).append(" ")
                .append(statusCode).append(" ")
                .append(reasonPhrase).append(" \r\n");
    }

    private void appendResponseHeaderField(StringBuilder responseMessage) {
        responseHeaderField.entrySet()
                .forEach(e -> responseMessage.append(e.getKey()).append(": ").append(e.getValue()).append("\r\n"));
    }
}
