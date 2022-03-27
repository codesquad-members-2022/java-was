package web.request;

public enum HttpMethod {
    GET,
    POST;

    public boolean isPost() {
        return this == HttpMethod.POST;
    }

    public boolean isGet() {
        return this == HttpMethod.GET;
    }
}
