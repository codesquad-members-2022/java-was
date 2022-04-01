package web.response;

import web.common.Cookie;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private DataOutputStream dos;
    private List<Cookie> cookies;

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
        cookies = new ArrayList<>();
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void redirectTo(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("location: " + redirectPath + "\r\n");
    }

    public void responseStaticResource(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        responseCookies();
        dos.writeBytes("\r\n");
        responseBody(body);
    }

    private void responseCookies() throws IOException {
        for (Cookie cookie : cookies) {
            dos.writeBytes(String.format("Set-Cookie: %s=%s;",cookie.getName(),cookie.getValue()));

            if (cookie.getMaxAge() != null) {
                dos.writeBytes("max-age="+cookie.getMaxAge()+";");
            }

            dos.writeBytes("path=/\r\n");
        }
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
