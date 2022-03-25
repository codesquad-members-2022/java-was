package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Response {

    private static final Logger log = LoggerFactory.getLogger(Response.class);

    private String requestLine;
    private URL url;
    private DataOutputStream dos;

    public Response(String requestLine, URL url, DataOutputStream dos) {
        this.requestLine = requestLine;
        this.url = url;
        this.dos = dos;
    }

    public void action() throws IOException {
        if (requestLine.contains("POST") && url.comparePath("/user/create")) {
            response302Header(url, dos);
        } else {
            byte[] body = Files.readAllBytes(new File("./webapp" + url.getPath()).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(URL url, DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Location: " + url.getPath() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
