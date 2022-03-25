package webserver.handler;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;
import webserver.Status;

import java.util.Map;

public class UserCreateHandler implements PathHandler {

    private static final Logger log = LoggerFactory.getLogger(UserCreateHandler.class);

    @Override
    public Response handle(Request request) {
        Map<String, String> body = request.getBody();

        User user = new User(
                body.get("userId"),
                body.get("password"),
                body.get("name"),
                body.get("email")
        );
        log.info("user={}", user);

        return new Response.Builder(Status.FOUND)
                .addHeader("Location", "http://localhost:8080/index.html")
                .build();
    }
}
