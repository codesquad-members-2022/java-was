package webserver;

import model.HttpStatus;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private HttpStatus httpStatus;
    private DataOutputStream dos;
    private Map<String, String> responseHeader = new HashMap<>();

    public HttpResponse(HttpStatus httpStatus, DataOutputStream dos) {
        this.httpStatus = httpStatus;
        this.dos = dos;
    }

    public void setHeader(String key, String value) {
        responseHeader.put(key, value);
    }

    public void sendWithBody(byte[] body) throws IOException {
        writeAllHeaders();
        writeBody(body);
        dos.flush();
    }

    public void send() throws IOException {
        writeAllHeaders();
        dos.flush();
    }

    private void writeAllHeaders() throws IOException {
        dos.writeBytes("HTTP/1.1 " + httpStatus.getStatusCode() + " " + httpStatus.getMessage() + "\r\n");
        if (!responseHeader.isEmpty()) {
            for (String key : responseHeader.keySet()) {
                dos.writeBytes(key + ": " + responseHeader.get(key) + "\r\n");
            }
        }
    }

    private void writeBody(byte[] body) throws IOException {
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
    }

}
