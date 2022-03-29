package webserver.handler;

import db.DataBase;
import model.User;
import webserver.Request;
import webserver.Response;
import webserver.Status;

public class UserLoginHandler implements PathHandler {
    @Override
    public Response handle(Request request) {
        try {
            User user = DataBase.findUserById(request.getBodyValue("userId"))
                    .orElseThrow(() -> {
                        throw new IllegalArgumentException("아이디를 다시 확인해주세요.");
                    });

            if (!user.checkPassword(request.getBodyValue("password"))) {
                throw new IllegalArgumentException("패스워드를 다시 확인해주세요.");
            }

            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .addHeader("Set-Cookie", "sessionId=" + user.getUserId() + "; Path=/")
                    .build();

        } catch (IllegalArgumentException e) {
            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/user/login_failed.html")
                    .build();
        }
    }
}
