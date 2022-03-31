package webserver.controller;

import db.SessionDataBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.response.Response;

public class LogoutController implements Controller{

    private static final String STATUS302 = "302 Redirect ";

    private final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @Override
    public Response handleRequest(Request request) {
        Response response = Response.of(request.getProtocol(), STATUS302);

        return logout(request, response);
    }

    private Response logout(Request request, Response response) {
        String sessionId = request.getSessionId();// sessionId="asdadsde";
        if(SessionDataBase.findSessionByUser(sessionId)) {
           SessionDataBase.remove(sessionId);
        }
        response.setLocation("/index.html");
        response.setCookie("sessionId", "deleted");
        return response;
    }


}
