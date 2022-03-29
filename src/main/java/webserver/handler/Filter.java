package webserver.handler;

import model.User;
import webserver.Request;
import webserver.Response;
import webserver.Status;

public class Filter {

    public Response handle(Request request) {

        // TODO: 로그인이 필요한 path 의 지정 방법
        if (!request.getPath().endsWith(".html")
            || request.getPath().matches("/user/login.html")
            || request.getPath().matches("/user/login_failed.html")
            || request.getPath().matches("/user/form.html")
            || request.getPath().matches("/index.html")
        ) {
            return null;
        }

        String sessionId = request.getCookieValue("sessionId");
        if (sessionId == null) {
            return getLoginFormRedirect();
        }

        User user = (User) request.getSessionAttribute(sessionId);
        if (user == null) {
            return getLoginFormRedirect();
        }

        request.setSessionAttribute("sessionId", user);
        return null;
    }

    public Response getLoginFormRedirect() {
        return new Response.Builder(Status.FOUND)
            .addHeader("Location", "http://localhost:8080/user/login.html")
            .build();
    }

}
