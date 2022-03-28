package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    private final String version;
    private HttpStatus httpStatus;
    private final Map<String, String> responseHeaders = new HashMap<>();
    private byte[] responseBody = new byte[]{};

    public HttpResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public int getHttpStatusCode() {
        return httpStatus.getStatusCode();
    }

    public String getHttpStatusMessage() {
        return httpStatus.getMessage();
    }

    public Map<String, String> getResponseHeaders() {
        return responseHeaders;
    }

    public String getHeader(String key) {
        return responseHeaders.get(key);
    }

    public Optional<byte[]> getResponseBody() {
        return Optional.of(responseBody);
    }

    public void addHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void addBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public HttpResponse ok(String url) throws IOException {
        this.httpStatus = HttpStatus.OK;
        this.addHeader("Content-Type", "text/html;charset=utf-8");
        this.addBody(Files.readAllBytes(new File("./webapp" + url).toPath()));
        log.debug("http response: {}", this);
        return this;
    }

    public HttpResponse badRequest() {
        this.httpStatus = HttpStatus.NOT_FOUND;
        this.addHeader("Content-Type", "text/html;charset=utf-8");
        log.debug("http response: {}", this);
        return this;
    }

    public HttpResponse redirect(String url) {
        this.httpStatus = HttpStatus.FOUND;
        this.addHeader("Content-Type", "text/html;charset=utf-8");
        this.addHeader("Location", url);
        log.debug("http response: {}, redirect: {}", this, url);
        return this;
    }

    @Override
    public String toString() {
        return "HttpResponse{ version=" + version + ", httpStatus=" + httpStatus.getStatusCode() + "}";
    }
}
