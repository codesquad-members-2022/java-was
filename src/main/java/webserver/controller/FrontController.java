package webserver.controller;

import webserver.request.Request;
import webserver.response.Response;

import java.util.Map;
import java.util.Optional;

public class FrontController implements Controller {

    private static final Map<RequestMapper, Controller> controllers =
            Map.of(new RequestMapper("POST", "/user/create"), new UserCreateController(),
                    new RequestMapper("POST", "/user/login"), new UserLoginController(),
                    new RequestMapper("GET", "/user/logout"), new LogoutController());

    @Override
    public Response handleRequest(Request request) {
        RequestMapper requestMapper = RequestMapper.from(request);

        Controller controller = Optional.ofNullable(controllers.get(requestMapper))
                .orElse(new ResourceController());

        return controller.handleRequest(request);
    }
}
