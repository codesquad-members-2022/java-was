package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FrontController {

    private static FrontController frontController = null;
    private static Map<String, Controller> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new UserCreateController());
        controllerMap.put("/user/login", new UserLoginController());
    }

    private FrontController() {}

    public static FrontController getInstance() {
        if (frontController == null) {
            frontController = new FrontController();
        }
        return frontController;
    }

    public void handleRequest(HttpRequest request, HttpResponse response) throws IOException {
        Controller controller;
        if (controllerMap.containsKey(request.getPath())) {
            controller = controllerMap.get(request.getPath());
        } else {
            controller = new DefaultController();
        }
        controller.service(request, response);
    }
}
