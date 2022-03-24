package util;

import java.util.HashMap;
import java.util.Map;

public class MimeUtils {
    private static final Map<String, String> contentType = new HashMap<>();
    private static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String CSS_CONTENT_TYPE = "text/css";

    static {
        contentType.put("html", HTML_CONTENT_TYPE);
        contentType.put("js", HTML_CONTENT_TYPE);
        contentType.put("css", CSS_CONTENT_TYPE);
    }

    public static String convertToContentType(String url) {
        String[] split = url.split("\\.");
        return contentType.get(split[split.length - 1]);
    }
}
