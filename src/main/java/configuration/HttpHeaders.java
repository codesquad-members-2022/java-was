package configuration;

import model.http.HeaderType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpHeaders {

    private static final Map<String, HeaderType> headerTypeMap;

    static {
        headerTypeMap = getHeaderType();
    }

    public static Map<String, HeaderType> getHeaderType() {
        Map<String, HeaderType> headerMap = new ConcurrentHashMap<>();
        for (String headerType : HeaderType.getHeaderTypes()) {
            headerMap.put(headerType, HeaderType.of(headerType));
        }
        return headerMap;
    }

    public HeaderType getHeaderType(String type) {
        return headerTypeMap.get(type);
    }
}
