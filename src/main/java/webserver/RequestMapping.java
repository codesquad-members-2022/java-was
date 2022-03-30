package webserver;

import java.util.Map;
import static java.util.Map.entry;

public class RequestMapping {

    private static final Map<String, Controller> controllers = Map.ofEntries(
            entry("/", new IndexController()),
            entry("/user/list", new UserListController()),
            entry("/user/create", new UserCreationController()),
            entry("/user/login", new UserLoginController()),
            entry("/user/logout", new UserLogoutController())
    );

    public static Controller getController(String path) {
        return controllers.getOrDefault(path, new StaticResourceController());
    }
}
