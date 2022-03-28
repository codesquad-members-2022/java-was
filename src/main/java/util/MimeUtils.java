package util;

import java.util.HashMap;
import java.util.Map;

public class MimeUtils {
    private static final String HTML_CONTENT_TYPE = "text/html;charset=utf-8";
    private static final String JS_CONTENT_TYPE = "application/javascript";
    private static final String CSS_CONTENT_TYPE = "text/css";
    private static final Map<String, String> contentType = new HashMap<>(
        Map.of("html", HTML_CONTENT_TYPE
            , "js", JS_CONTENT_TYPE
            , "css", CSS_CONTENT_TYPE)
    );

    public static String convertToContentType(String url) {
        String[] split = url.split("\\.");
        return contentType.get(split[split.length - 1]);
    }
}
