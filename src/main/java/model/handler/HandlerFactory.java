package model.handler;

import model.handler.controller.HomeController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlerFactory {

    private HandlerFactory (){};

    private final Map<String, Handler> controllers = new ConcurrentHashMap<>();

    void initMapping() {
        put("/index", new HomeController());
    }

    private void put(String url, Handler handler) {
        controllers.put(url, handler);
    }
}
