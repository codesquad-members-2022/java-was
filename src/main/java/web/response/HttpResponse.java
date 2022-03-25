package web.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public class HttpResponse {

    private DataOutputStream dos;
    private byte[] body;

    public HttpResponse(OutputStream out) {
        dos = new DataOutputStream(out);
    }

    public void redirectTo(String redirectPath) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("location: " + redirectPath + "\r\n");
    }

    public void responseStaticResource(String path) throws IOException {
        body = Files.readAllBytes(new File("./webapp" + path).toPath());
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + body.length + "\r\n");
        dos.writeBytes("\r\n");
        responseBody();
    }

    private void responseBody() throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }
}
