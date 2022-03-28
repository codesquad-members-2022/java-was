package webserver.handler;

import java.util.HashMap;
import java.util.Map;

public class PathMapperFactory {

    private static Map<Pair, PathHandler> requestMap = new HashMap<>();

    public static PathMapper create() {
        requestMap.put(new Pair("POST", "/user/create"), new UserCreateHandler());

        return new PathMapper(requestMap, new DefaultFileHandler());
    }

}
