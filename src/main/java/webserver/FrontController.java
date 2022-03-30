package webserver;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import webserver.controller.*;

public class FrontController {

	private static FrontController instance = new FrontController();
	private final Map<String, Controller> controllerMap = new ConcurrentHashMap<>();

	private FrontController() {
		controllerMap.put("/user/create", UserCreateController.getInstance());
		controllerMap.put("/user/login", LoginController.getInstance());
		controllerMap.put("/user/logout", LogoutController.getInstance());
		controllerMap.put("*", DefaultController.getInstance());
	}

	public static FrontController getInstance() {
		return instance;
	}

	public void service(Request request, Response response) throws IOException {
		Controller controller = controllerMap.get(request.getUri());
		if (controller == null) {
			controller = controllerMap.get("*");
		}

		controller.process(request, response);
	}
}
