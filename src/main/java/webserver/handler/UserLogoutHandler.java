package webserver.handler;

import webserver.Request;
import webserver.Response;
import webserver.Status;

public class UserLogoutHandler implements PathHandler {

    @Override
    public Response handle(Request request) {
        String sessionId = request.getCookieValue("sessionId");

        return new Response.Builder(Status.FOUND)
                .addHeader("Location", "http://localhost:8080/index.html")
                .addHeader("Set-Cookie", "sessionId=" + sessionId
                        + "; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT")
                .build();
    }
}
