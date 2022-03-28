package webserver.controller;

import webserver.request.Request;

import java.util.Objects;

public class RequestMapper {

    private String httpMethod;
    private String url;

    public RequestMapper(String httpMethod, String url) {
        this.httpMethod = httpMethod;
        this.url = url;
    }

    public static RequestMapper from(Request request) {
        return new RequestMapper(request.getHttpMethod(), request.getUrl());
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMapper that = (RequestMapper) o;
        return getHttpMethod().equals(that.getHttpMethod()) && getUrl().equals(that.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHttpMethod(), getUrl());
    }
}
