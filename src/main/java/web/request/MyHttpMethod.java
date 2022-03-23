package web.request;

public enum MyHttpMethod {
    GET,
    POST;

    public boolean isPost() {
        return this == MyHttpMethod.POST;
    }

    public boolean isGet() {
        return this == MyHttpMethod.GET;
    }
}
