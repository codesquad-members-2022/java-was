package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {
    private static final Logger log = LoggerFactory.getLogger(Response.class);
    private final byte[] body;
    private final String method;
    private final String url;

    public Response(String method, String url) {
        this.method = method;
        this.url = url;
        this.body = getFile();
    }

    private byte[] getFile() {
        byte[] fileData = new byte[0];
        try {
            fileData = Files.readAllBytes(new File("./webapp" + url).toPath());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return fileData;
    }

    public void body(DataOutputStream dos) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response200Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(DataOutputStream dos, String responseUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + "http://localhost:8080/" + responseUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
