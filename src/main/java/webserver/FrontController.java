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
		if ("/user/create".equals(request.getUri())) {	// POST /user/create
			Controller userController = controllerMap.get("/user/create");
			userController.process(request, response);
		} else if ("/user/login".equals(request.getUri())) { // POST /user/login
			Controller loginController = controllerMap.get("/user/login");
			loginController.process(request, response);
		} else if ("/user/logout".equals(request.getUri())) { // GET /user/logout
			Controller logoutController = controllerMap.get("/user/logout");
			logoutController.process(request, response);
		} else { // GET *
			Controller defaultController = controllerMap.get("*");
			defaultController.process(request, response);
		}
	}
}
