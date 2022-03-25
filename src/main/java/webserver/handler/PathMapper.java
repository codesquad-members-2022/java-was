package webserver.handler;

import webserver.Request;
import webserver.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PathMapper {

    private static final Map<Pair, PathHandler> requestMap = new HashMap<>();
    private static final PathHandler defaultFileHandler = new DefaultFileHandler();

    static {
        requestMap.put(new Pair("POST", "/user/create"), new UserCreateHandler());
    }

    public Response callHandler(Request request) {
        Pair pair = Pair.from(request);
        PathHandler pathHandler = Optional.ofNullable(requestMap.get(pair))
                .orElse(defaultFileHandler);

        return pathHandler.handle(request);
    }

    private static class Pair {
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
}
