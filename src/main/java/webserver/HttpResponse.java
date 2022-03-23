package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {

    private int statusCode;
    private DataOutputStream dos;
    private Map<String, String> responseHeader = new HashMap<>();

    public HttpResponse(int statusCode, DataOutputStream dos) {
        this.statusCode = statusCode;
        this.dos = dos;
    }

    public void setHeader(String key, String value) {
        responseHeader.put(key, value);
    }

    public void writeBody(byte[] body) throws IOException {
        writeAllHeaders();
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        dos.write(body, 0, body.length);
    }

    public void writeLocation(String location) throws IOException {
        writeAllHeaders();
        dos.writeBytes("Location: " + location);
        dos.writeBytes("\r\n");
    }

    public void send() throws IOException {
        dos.flush();
    }

    private void writeAllHeaders() throws IOException {
        dos.writeBytes("HTTP/1.1 " + statusCode + " OK \r\n");
        if (!responseHeader.isEmpty()) {
            for (String key : responseHeader.keySet()) {
                dos.writeBytes(key + ": " + responseHeader.get(key) + "\r\n");
            }
        }
    }

}
