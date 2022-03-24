package webserver;

import model.HeaderType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration {
    public Map<String, HeaderType> getHeaderType() {
        Map<String, HeaderType> headerMap = new ConcurrentHashMap<>();
        for (String headerType : HeaderType.getHeaderTypes()) {
            headerMap.put(headerType, HeaderType.of(headerType));
        }
        return headerMap;
    }
}
