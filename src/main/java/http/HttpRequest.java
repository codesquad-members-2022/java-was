package http;

import java.util.Map;

public class HttpRequest {
    private final String method;
    private final String path;
    private final String protocol;
    private final String body;
    private Map<String, String> header;

    public HttpRequest(Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.protocol = builder.protocol;
        this.body = builder.body;
        this.header = builder.header;
    }

    public static class Builder{
        private String method;
        private String path;
        private String protocol;
        private String body;
        private Map<String, String> header;

        public Builder method (String method){
            this.method = method;
            return this;
        }

        public Builder path (String path){
            this.path = path;
            return this;
        }

        public Builder protocol (String protocol){
            this.protocol = protocol;
            return this;
        }

        public Builder body (String body){
            this.body = body;
            return this;
        }

        public Builder header (Map<String, String> header){
            this.header = header;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getBody() {
        return body;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", protocol='" + protocol + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
