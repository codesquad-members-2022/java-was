package webserver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Response {

    private final Map<String, Object> responseHeaderMap = new LinkedHashMap<>();
    private String body;

    public Response() {
        responseHeaderMap.put("Status-Line", StatusCode.SUCCESSFUL_200);
    }

    public void setStatus(StatusCode statusCode) {
        responseHeaderMap.put("Status-Line", statusCode);
    }

    public void setContentType(String type) {
        responseHeaderMap.put("Content-Type", type);
    }

    public void setRedirect(StatusCode redirectionStatusCode, String redirectionPath) {
        responseHeaderMap.put("Status-Line", redirectionStatusCode);
        responseHeaderMap.put("Location", redirectionPath);
    }

    public void setBody(byte[] responseBody, String contentType) {
        body = new String(responseBody, StandardCharsets.UTF_8);
        responseHeaderMap.put("Content-Length", responseBody.length);

        contentType = parseContentType(contentType);
        responseHeaderMap.put("Content-Type", contentType);
    }

    private String parseContentType(String contentType) {
        if (contentType.equals("html")) {
            return "text/html";
        } else if (contentType.equals("js")) {
            return "application/js";
        } else if (contentType.equals("css")) {
            return "text/css";
        } else {
            return "";
        }
    }

    public void setBody(String responseBody, String contentType) {
        body = responseBody;
        responseHeaderMap.put("Content-Length", responseBody.length());
        responseHeaderMap.put("Content-Type", contentType);
    }

    public String toHeader() {
        List<String> header = new ArrayList<>();

        for (Entry<String, Object> entry : responseHeaderMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("Status-Line")) {
                StatusCode statusCode = (StatusCode) value;
                header.add("HTTP/1.1" + " " + statusCode.getCode() + " " + statusCode.getMessage()
                        + "\r\n");
            } else {
                header.add(key + ": " + value + "\r\n");
            }
        }
        header.add("\r\n");
        StringBuffer sb = new StringBuffer();
        for (String line : header) {
            sb.append(line);
        }
        return sb.toString();
    }


    public String toBody() {
        return body + "\r\n";
    }
}
