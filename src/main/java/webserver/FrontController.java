package webserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import webserver.controller.Controller;
import webserver.controller.DefaultController;
import webserver.controller.LoginController;
import webserver.controller.UserCreateController;

public class FrontController {

	private static FrontController instance = new FrontController();
	private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

	private FrontController() {
		controllerMap.put("/user/create", UserCreateController.getInstance());
		controllerMap.put("/user/login", LoginController.getInstance());
		controllerMap.put("*", DefaultController.getInstance());
	}

	public static FrontController getInstance() {
		return instance;
	}

	public void service(Request request, Response response) throws IOException {
		// /user/create Controller
		if ("/user/create".equals(request.getUri())) {
			Controller userController = controllerMap.get("/user/create");
			userController.process(request, response);
		} else if ("/user/login".equals(request.getUri())) {
			Controller loginController = controllerMap.get("/user/login");
			loginController.process(request, response);
		} else { // 그 외 Controller
			Controller defaultController = controllerMap.get("*");
			defaultController.process(request, response);
		}
	}
}
