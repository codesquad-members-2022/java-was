package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String FILE_EXTENSION_SEPARATOR = ".";
    private final DataOutputStream dos;

    HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void responseStaticResource(String path) {
        try {
            byte[] body = Files.readAllBytes(Path.of(path));
            String contentType = getContentType(path);
            response200Header(body.length, contentType);
            responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContentType(String path) {
        String extension = getFileExtension(path);
        return ContentTypeMapping.getContentType(extension);
    }

    private String getFileExtension(String path) {
        int index = path.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        return index == -1 ? "" : path.substring(index);
    }

    public void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
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
