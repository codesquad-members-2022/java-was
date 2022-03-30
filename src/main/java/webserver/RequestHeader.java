package webserver;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHeader {
    private static final Logger log = LoggerFactory.getLogger(RequestHeader.class);
    private static final String[] methods = {"GET", "POST"};
    public static final String SEPARATOR_OF_SPACE = "\\p{Blank}";
    public static final String SEPARATOR_OF_QUERY_STRINGS = "?";
    public static final String SEPARATOR_OF_COLON = ":";
    public static final String ROOT = "/";
    public static final String ROOT_PATH = ROOT + "index.html";
    public static final String CONTENT_LENGTH = "Content-Length";

    private String method;
    private String path;
    private String version;
    private Map<String, String> header;

    public RequestHeader(List<String> messages) {
        String[] firstLine = messages.get(0).split(SEPARATOR_OF_SPACE);
        setMethod(firstLine[0]);
        setPath(firstLine[1]);
        this.version = firstLine[2].trim();
        setHeader(messages);
    }

    public boolean isGetMethod() {
        return this.method.equals(methods[0]);
    }

    public boolean isPostMethod() {
        return this.method.equals(methods[1]);
    }

    private void setPath(String path) {
        if (path.trim().equals(ROOT) || Strings.isNullOrEmpty(path)) {
            this.path = ROOT_PATH;
            return;
        }
        this.path = path.trim();
    }

    private void setHeader(List<String> messages) {
        this.header = new HashMap<>();
        for (int i = 1; i < messages.size(); i++) {
            String line = messages.get(i);
            if (line.indexOf(SEPARATOR_OF_COLON) > 0) {
                String[] splitHeader = line.split(SEPARATOR_OF_COLON);
                String key = splitHeader[0].trim();
                String value = splitHeader[1].trim();
                this.header.put(key, value);
            }
        }
    }

    private void setMethod(String methodStr) {
        try {
            String method = methodStr.toUpperCase().trim();
            for (String m : methods) {
                if (m.equals(method)) {
                    this.method = method;
                    return;
                }
            }
            throw new IOException("Invalid Method : " + method);
        } catch (IOException exception) {
            log.error(exception.getMessage());
        }
    }

    public String getQueryParams() {
		return path.substring(path.indexOf(SEPARATOR_OF_QUERY_STRINGS) + 1);
    }

    // POST
    public int contentLength() {
        return Integer.parseInt(this.header.get(CONTENT_LENGTH));
    }

    public String getPath() {
        return this.path;
    }

    public String getVersion() {
        return version;
    }

    public String getCookie() {
        return header.get("Cookie");
    }
}
