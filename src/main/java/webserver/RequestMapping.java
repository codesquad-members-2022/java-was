package webserver;

import java.util.Map;
import static java.util.Map.entry;

public class RequestMapping {

    private static final Controller STATIC_RESOURCE_CONTROLLER = new StaticResourceController();

    private static final Map<String, Controller> controllers = Map.ofEntries(
            entry("/", new IndexController()),
            entry("/comment/create", new CommentCreationController()),
            entry("/user/list", new UserListController()),
            entry("/user/create", new UserCreationController()),
            entry("/user/login", new UserLoginController()),
            entry("/user/logout", new UserLogoutController())
    );

    public static void runController(HttpRequest httpRequest, HttpResponse httpResponse) {
        controllers.getOrDefault(httpRequest.getPath(), STATIC_RESOURCE_CONTROLLER)
                .process(httpRequest, httpResponse);
    }
}
