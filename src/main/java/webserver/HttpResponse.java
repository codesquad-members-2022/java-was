package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private final DataOutputStream dos;
    private final String resourceRoot;

    HttpResponse(DataOutputStream dos, String resourceRoot) {
        this.dos = dos;
        this.resourceRoot = resourceRoot;
    }

    public void responseStaticResource(String path) {
        try {
            byte[] body = Files.readAllBytes(Path.of(resourceRoot + path));
            response200Header(body.length);
            responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void response200Header(int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302Header(String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderAfterLogin(String redirectPath, String sessionId) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Set-Cookie: sessionId=" + sessionId + "; Path=/\r\n");
            dos.writeBytes("Set-Cookie: logged_in=true; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderAfterLogout(String redirectPath, String sessionId) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Set-Cookie: sessionId=" + sessionId + "; Max-Age=0; Path=/\r\n");
            dos.writeBytes("Set-Cookie: logged_in=false; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
