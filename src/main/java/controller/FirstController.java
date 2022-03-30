package controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import webserver.ControllerMapper;
import webserver.HttpMethod;
import webserver.Request;
import webserver.Response;

public class FirstController {

	//todo
	// map 으로 controllermapper, 컨트롤러 매칭 (컨트롤러들을 넣겠다 put)
	// 매칭된 컨트롤러 찾아서(get) 실행

	private static final FirstController instance = new FirstController();

	private final Map<ControllerMapper, Controller> map = new ConcurrentHashMap<>();

	private FirstController() {
		map.put(new ControllerMapper(HttpMethod.GET, "/index.html"), HomeController.getInstance());
	}

	public void run(Request request, Response response) {
		Controller controller = map.get(new ControllerMapper(request.getHttpMethod(), request.getPath()));
		controller.process(request, response);
		response.responseBody();
	}

	public static FirstController getInstance() {
		return instance;
	}

}
