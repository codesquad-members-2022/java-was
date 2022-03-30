package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private DataOutputStream dos;
    private byte[] body;

    public HttpResponse(OutputStream out) {
        this.dos = new DataOutputStream(out);
    }


    public void response302WithExpiredCookieHeader(String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("Set-Cookie: sessionId=" + cookie + "; Max-Age=-1;  path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302WithCookieHeader(String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("Set-Cookie: sessionId=" + cookie + "; path=/");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080/" + path + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header() {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response200Header() throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + body.length + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void writeBody(String path) {
        try {
            body = IOUtils.readRequestResource(path);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody() {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
