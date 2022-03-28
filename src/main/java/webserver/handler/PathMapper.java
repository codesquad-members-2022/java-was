package webserver.handler;

import webserver.Request;
import webserver.Response;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PathMapper {


    private final Map<Pair, PathHandler> requestMap;
    private final PathHandler defaultFileHandler;

    public PathMapper(Map<Pair, PathHandler> requestMap, PathHandler defaultFileHandler) {
        this.requestMap = requestMap;
        this.defaultFileHandler = defaultFileHandler;
    }

    public Response callHandler(Request request) {
        Pair pair = Pair.from(request);
        PathHandler pathHandler = Optional.ofNullable(requestMap.get(pair))
                .orElse(defaultFileHandler);

        return pathHandler.handle(request);
    }

}
