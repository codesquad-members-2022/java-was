package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {

    private byte[] body;
    private String headers;
    private String requestLine;
    private URL url;

    public Response(String requestLine, URL url) {
        this.requestLine = requestLine;
        this.url = url;
    }

    public void action() throws IOException {
        if (requestLine.contains("POST") && requestLine.contains("/user/create")) {
            headers = response302Header(url);
        } else {
            body = Files.readAllBytes(new File("./webapp" + url.getPath()).toPath());
            headers = response200Header(body.length);
        }
    }

    private String response200Header(int lengthOfBodyContent) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Content-Length: " + lengthOfBodyContent + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

    private String response302Header(URL url) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Location: " + url.getPath() + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }

    public String getHeaders() {
        return headers;
    }
}
