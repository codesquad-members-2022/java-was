package webserver.handler;

import db.DataBase;
import model.User;
import webserver.Request;
import webserver.Response;
import webserver.Status;

public class UserCreateHandler implements PathHandler {

    @Override
    public Response handle(Request request) {

        try {
            DataBase.findUserById(request.getBodyValue("userId"))
                    .ifPresent(findUser -> {
                        throw new IllegalArgumentException("중복된 유저입니다.");
                    });

            User user = new User(
                request.getBodyValue("userId"),
                request.getBodyValue("password"),
                request.getBodyValue("name"),
                request.getBodyValue("email")
            );
            DataBase.addUser(user);

            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .addHeader("Set-Cookie", "sessionId=" + user.getUserId() + "; Path=/")
                .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder(Status.FOUND)
                .addHeader("Location", "http://localhost:8080/user/form.html")
                .build();
        }
    }


}
