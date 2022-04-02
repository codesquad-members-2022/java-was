package model.handler.controller;

import model.handler.Handler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static configuration.ObjectFactory.*;

public class HandlerFactory {

    private static final Map<String, Handler> controllers = new ConcurrentHashMap<>();

    private HandlerFactory (){
        initMapping();
    };

    private static final HandlerFactory instance = new HandlerFactory();

    public static HandlerFactory getInstance() {
        if (instance == null) {
            return new HandlerFactory();
        }
        return instance;
    }

    public static Handler getHandler(String requestURL) {
        Handler findHandler = controllers.get(requestURL);
        if (findHandler == null) {
            return homeController;
        }
        return findHandler;
    }

    void initMapping() {
        put("/index", homeController);
        put("/user/create", userCreateController);
        put("/user/login", userLoginController);
    }

    private void put(String url, Handler handler) {
        controllers.put(url, handler);
    }
}
