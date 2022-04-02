package configuration;

import model.http.HeaderType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HttpHeaders {

    private static final Map<String, String> headerTypeMap;

    private HttpHeaders (){};
    private static final HttpHeaders instance = new HttpHeaders();

    public static HttpHeaders getInstance() {
        if (instance == null) {
            return new HttpHeaders();
        }
        return instance;
    }


    static {
        headerTypeMap = getHeaderType();
    }

    private static Map<String, String> getHeaderType() {
        Map<String, String> headerMap = new ConcurrentHashMap<>();
        for (String headerType : HeaderType.getHeaderTypes()) {
            headerMap.put(headerType, headerType);
        }
        return headerMap;
    }

    public String getHeaderType(String type) {
        return headerTypeMap.get(type);
    }
}
