package webserver.controller;

import db.SessionDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;

public class LogoutController implements BackController {

    private static final String STATUS302 = "302 Redirect ";

    private final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @Override
    public Response process(Request request) {
        Response response = new Response(request.getProtocol(), STATUS302);

        return logout(request, response);
    }

    private Response logout(Request request, Response response) {
        String sessionId = request.getSessionId();
        if (SessionDataBase.findSessionByUser(sessionId)) {
            SessionDataBase.remove(sessionId);
        }

        response.setCookie("sessionId=" + sessionId + "; Max-age=0; path=/;");
        response.setLocation("/index.html");
        return response;
    }


}
