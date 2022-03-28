package webserver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMapping {

    private static Map<String, Controller> controllers = new ConcurrentHashMap<>();

    static {
        controllers.put("/user/list", new UserListController());
        controllers.put("/user/create", new UserCreationController());
        controllers.put("/user/login", new UserLoginController());
        controllers.put("/user/logout", new UserLogoutController());
    }

    public static Controller getController(String path) {
        return controllers.getOrDefault(path, new StaticResourceController());
    }
}
