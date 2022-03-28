package webserver.handler;

import webserver.Request;

import java.util.Objects;

public class Pair {
    private final String method;
    private final String path;

    public Pair(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public static Pair from(Request request) {
        return new Pair(request.getMethod(), request.getPath());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        Pair pair = (Pair) o;
        return method.equals(pair.method) && path.equals(pair.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(method, path);
    }
}
