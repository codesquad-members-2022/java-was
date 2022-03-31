package webserver;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RequestLine {

    private String methodType;
    private URL url;

    public RequestLine(String methodType, String url) {
        this.methodType = methodType;
        this.url = makeURL(url);
    }

    private URL makeURL(String url) {
        String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
        return new URL(decodedUrl);
    }

    public boolean isGetMethodType() {
        return "GET".equals(methodType);
    }

    public boolean isPostMethodType() {
        return "POST".equals(methodType);
    }

    public URL getUrl() {
        return url;
    }
}
