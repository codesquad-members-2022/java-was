package model.handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static configuration.ObjectFactory.homeController;

public class HandlerFactory {

    private HandlerFactory (){
        initMapping();
    };

    private static final Map<String, Handler> controllers = new ConcurrentHashMap<>();

    public static Handler getHandler(String requestURL) {
        Handler findHandler = controllers.get(requestURL);
        if (findHandler == null) {
            return homeController;
        }
        return findHandler;
    }

    void initMapping() {
        put("/index", homeController);
    }

    private void put(String url, Handler handler) {
        controllers.put(url, handler);
    }
}
