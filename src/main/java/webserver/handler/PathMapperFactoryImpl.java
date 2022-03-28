package webserver.handler;

import java.util.HashMap;
import java.util.Map;

public class PathMapperFactoryImpl implements PathMapperFactory {

    @Override
    public PathMapper create() {
        Map<Pair, PathHandler> handlerMap = new HashMap<>() {{
            put(new Pair("POST", "/user/create"), new UserCreateHandler());
        }};
        return new PathMapper(handlerMap, new DefaultFileHandler());
    }
}
