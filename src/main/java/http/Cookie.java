package http;

public class Cookie {
    private final String name;
    private final String value;
    private String path;
    private Integer maxAge;

    public Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean isPathSet() {
        return (path != null && !path.equals(""));
    }

    public boolean isMaxAgeSet() {
        return (maxAge != null);
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getPath() {
        return path;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value);
        if (isPathSet()) sb.append(";").append("Path=").append(path);
        if (isMaxAgeSet()) sb.append(";").append("Max-Age=").append(maxAge);

        return sb.toString();
    }
}
