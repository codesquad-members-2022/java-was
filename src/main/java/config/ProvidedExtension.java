package config;

import java.util.HashMap;
import java.util.Map;

public class ProvidedExtension {
    private static final Map<String, String> extensionMapping = new HashMap<>();
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";

    static {
        extensionMapping.put("html", "text/html");
        extensionMapping.put("css", "text/css");
        extensionMapping.put("js", "text/javascript");
        extensionMapping.put("woff", "application/font-woff");
        extensionMapping.put("ttf", "font/ttf");
    }

    private ProvidedExtension() {
    }

    public static String extensionResolver(String extension) {
        return extensionMapping.getOrDefault(extension, DEFAULT_CONTENT_TYPE);
    }
}
