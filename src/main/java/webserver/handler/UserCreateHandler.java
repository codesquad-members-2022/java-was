package webserver.handler;

import model.User;
import service.UserService;
import webserver.Request;
import webserver.Response;
import webserver.Status;
import webserver.mapper.Session;

public class UserCreateHandler implements PathHandler {

    private static final Session session = Session.getInstance();
    private static final UserService userService = UserService.getInstance();

    @Override
    public Response handle(Request request) {

        try {
            User user = new User(
                    request.getBodyValue("userId"),
                    request.getBodyValue("password"),
                    request.getBodyValue("name"),
                    request.getBodyValue("email")
            );
            userService.register(user);

            String sessionId = session.setUser(user);

            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .addHeader("Set-Cookie", "sessionId=" + sessionId + "; Path=/")
                .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder(Status.FOUND)
                .addHeader("Location", "http://localhost:8080/user/form.html")
                .build();
        }
    }
}
