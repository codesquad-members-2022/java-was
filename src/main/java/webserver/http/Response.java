package webserver.http;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.MimeUtils;

public class Response {
    private static Logger log = LoggerFactory.getLogger(Response.class);
    private final DataOutputStream dataOutputStream;

    public Response(OutputStream out) {
        this.dataOutputStream = new DataOutputStream(out);
    }

    public void response302Header(String url) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dataOutputStream.writeBytes("Location: " + url + " \r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response302HeaderWithCookie(String url, String cookie) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dataOutputStream.writeBytes("Location: " + url + " \r\n");
            dataOutputStream.writeBytes("Set-Cookie: " + cookie + "; Path=/\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void response200Header(int lengthOfBodyContent, String url) {
        try {
            dataOutputStream.writeBytes("HTTP/1.1 200 OK \r\n");
            dataOutputStream.writeBytes("Content-Type: " + MimeUtils.convertToContentType(url) + "\r\n");
            dataOutputStream.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dataOutputStream.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void responseBody(byte[] body) {
        try {
            dataOutputStream.write(body, 0, body.length);
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
