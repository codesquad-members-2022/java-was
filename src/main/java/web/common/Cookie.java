package web.common;

import util.Pair;

public class Cookie {

    private String name;
    private String value;
    private Integer maxAge;

    public Cookie(Pair<String, String> pair) {
        this.name = pair.getKey();
        this.value = pair.getValue();
    }

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public Integer getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
