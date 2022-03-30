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

    public Optional<byte[]> getResponseBody() {
        return Optional.of(responseBody);
    }

    public void addHeader(String key, String value) {
        responseHeaders.put(key, value);
    }

    public void addBody(byte[] responseBody) {
        this.responseBody = responseBody;
    }

    public HttpResponse ok(String url) {
        this.httpStatus = HttpStatus.OK;
        this.addHeader("Content-Type", "text/html;charset=utf-8");
        try {
            this.addBody(Files.readAllBytes(new File("./webapp" + url).toPath()));
        } catch (IOException exception) {
            log.error("error of http's response: {}", exception.getMessage());
            return this.badRequest();
        }
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
    
    public String headers() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%s %d %s %s", this.version, this.getHttpStatusCode(), this.getHttpStatusMessage(), System.lineSeparator()));
        Map<String, String> responseHeaders = this.responseHeaders;
        for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
            sb.append(String.format("%s: %s %s", entry.getKey(), entry.getValue(), System.lineSeparator()));
        }
        return sb.toString();
    }

    private int getHttpStatusCode() {
        return httpStatus.getStatusCode();
    }

    private String getHttpStatusMessage() {
        return httpStatus.getMessage();
    }
    
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public String toString() {
        return "HttpResponse{ version=" + version + ", httpStatus=" + httpStatus.getStatusCode() + "}";
    }

    public String bodyLength() {
        return String.valueOf(this.responseBody.length);
    }
}
