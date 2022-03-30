package webserver.handler;

import model.User;
import service.UserService;
import webserver.Request;
import webserver.Response;
import webserver.Status;
import webserver.mapper.Session;

public class UserLoginHandler implements PathHandler {

    private final Session session = Session.getInstance();
    private final UserService userService = UserService.getInstance();

    @Override
    public Response handle(Request request) {
        try {
            User user = userService.login(
                    request.getBodyValue("userId"),
                    request.getBodyValue("password")
            );

            String sessionId = session.setUser(user);

            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .addHeader("Set-Cookie", "sessionId=" + sessionId + "; Path=/")
                    .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/user/login_failed.html")
                    .build();
        }
    }
}
