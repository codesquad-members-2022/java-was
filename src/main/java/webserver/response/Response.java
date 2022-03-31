package webserver.response;

import org.checkerframework.checker.units.qual.C;
import webserver.ContentType;
import webserver.Cookie;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Response {
    private static final String ROOT_PATH = "./webapp";

    private final String protocol;
    private final String status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;


    public Response(String protocol, String status) {
        this.protocol = protocol;
        this.status = status;
        this.viewPath = viewPath;
        this.contentType = contentType;
        this.location = location;
        this.cookie = new Cookie();
    }

    public static Response of(String protocol, String status, String viewPath, ContentType contentType) {
        return new Response(protocol, status, viewPath, contentType, null);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getStatus() {
        return status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public Optional<byte[]> getBody() {
        return Optional.ofNullable(body);
    }

    public void setLocation(String location) {
        headers.put("Location", location);
    }

    public void setCookie(String cookie) {
        headers.put("Set-Cookie", cookie);
    }

    public void saveBody(String viewPath) {
        try {
            this.body = Files.readAllBytes(new File(ROOT_PATH + viewPath).toPath());
            headers.put("Content-Type", ContentType.from(viewPath).getType() + "; charset=utf-8");
            headers.put("Content-Length", String.valueOf(body.length));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
