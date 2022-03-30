package webserver;

import java.util.HashMap;
import java.util.Map;

public class Cookie {
    private final Map<String, String> cookies = new HashMap<>();

    public void setCookies(String key, String value) {
        cookies.put(key, value);
    }


}
