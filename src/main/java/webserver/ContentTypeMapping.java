package webserver;

import java.util.Map;
import static java.util.Map.entry;

public class ContentTypeMapping {

    private static final String CHARSET_UTF_8 = ";charset=utf-8";

    private static final Map<String, String> contentTypes = Map.ofEntries(
            entry(".html", "text/html"),
            entry(".js", "text/javascript"),
            entry(".css", "text/css"),
            entry(".ico", "image/x-icon")
    );

    public static String getContentType(String fileExtension) {
        String contentType = contentTypes.getOrDefault(fileExtension, "*/*");

        return contentType.startsWith("text") ? contentType.concat(CHARSET_UTF_8) : contentType;
    }
}
