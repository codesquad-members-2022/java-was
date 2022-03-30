package controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ControllerMapper;
import webserver.HttpMethod;
import webserver.Request;
import webserver.Response;

public class FirstController {

	private static final FirstController instance = new FirstController();

	private final Map<ControllerMapper, Controller> map = new ConcurrentHashMap<>();
	private Logger log = LoggerFactory.getLogger(FirstController.class);

	private FirstController() {
		map.put(new ControllerMapper(HttpMethod.POST, "/user/create"), UserJoinController.getInstance());
		map.put(new ControllerMapper(HttpMethod.GET, "/user/logout"), UserLogoutController.getInstance());
		map.put(new ControllerMapper(HttpMethod.POST, "/user/login"), UserLoginController.getInstance());
	}

	public void run(Request request, Response response) throws IOException {
		Controller controller = map.get(new ControllerMapper(request.getHttpMethod(), request.getPath()));
		if (controller == null) {
			controller = HomeController.getInstance();
		}
		log.debug("call {}", controller);
		controller.process(request, response);
	}

	public static FirstController getInstance() {
		return instance;
	}

}
