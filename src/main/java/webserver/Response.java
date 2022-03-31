package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Response {

    private byte[] body;
    private String headers;
    private String sessionId;
    private RequestLine requestLine;
    private URL url;

    public Response(RequestLine requestLine, String sessionId) {
        this.requestLine = requestLine;
        this.url = requestLine.getUrl();
        this.sessionId = sessionId;
    }

    public void action() throws IOException {
        if (requestLine.isPostMethodType()) {
            if (url.comparePath("/user/create")) {
                headers = response302Header(url);
            }
            if (url.comparePath("/user/login")) {
                headers = response302Header(url, sessionId, true);
            }
        }
        if (requestLine.isGetMethodType()) {
            if (url.comparePath("/user/logout")) {
                headers = response302Header(url, sessionId, false);
            } else {
                body = Files.readAllBytes(new File("./webapp" + url.getPath()).toPath());
                headers = response200Header(body.length);
            }
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
        sb.append("Location: " + url.getRedirectPath() + "\r\n");
        sb.append("\r\n");

        return sb.toString();
    }

    private String response302Header(URL url, String sessionId, boolean isLogin) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 302 Found \r\n");
        sb.append("Content-Type: text/html;charset=utf-8\r\n");
        sb.append("Location: " + url.getRedirectPath() + "\r\n");

        if (isLogin) {
            sb.append("Set-Cookie: sessionId = " + sessionId + "; Path=/\r\n");
        } else {
            sb.append("Set-Cookie: sessionId = " + sessionId + "; max-age=0; Path=/\r\n");
        }
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
