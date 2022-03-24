package webserver;

import model.HeaderType;

import java.util.Map;

public class Request {

    private String UUID;
    private Map<HeaderType, String> map;
    private String userId;
    private String password;
    private String name;
    private String email;

    public Request(Map<HeaderType, String> map, String userId, String password, String name, String email) {
        this.UUID = createUUID();
        this.map = map;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    private String createUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    public String getHeaderType(HeaderType type) {
        return map.get(type);
    }
}
