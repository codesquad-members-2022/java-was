package webserver.response;

import webserver.ContentType;
import webserver.Cookie;

public class Response {

    private final String protocol;
    private final String status;
    private String viewPath;
    private ContentType contentType;
    private String location;
    private final Cookie cookie;

    private Response(String protocol, String status, String viewPath, ContentType contentType, String location) {
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

    public static Response of(String protocol, String status, String location) {
        return new Response(protocol, status, null, null, location);
    }


    public String getProtocol() {
        return protocol;
    }

    public String getStatus() {
        return status;
    }

    public String getViewPath() {
        return viewPath;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }

    public String getContentTypeAsString() {
        return contentType.getType();
    }

    public void setCookie(String key, String value) {
        this.cookie.setCookies(key, value);
    }
}
