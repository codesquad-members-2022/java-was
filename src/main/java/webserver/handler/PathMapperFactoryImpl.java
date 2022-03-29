package webserver.handler;

import java.util.HashMap;
import java.util.Map;

public class PathMapperFactoryImpl implements PathMapperFactory {

    @Override
    public PathMapper create() {
        Map<Pair, PathHandler> handlerMap = new HashMap<>() {{
            put(new Pair("POST", "/user/create"), new UserCreateHandler());
            put(new Pair("POST", "/user/login"), new UserLoginHandler());
            put(new Pair("GET", "/user/logout"), new UserLogoutHandler());
        }};
        return new PathMapper(handlerMap, new DefaultFileHandler());
    }
}
