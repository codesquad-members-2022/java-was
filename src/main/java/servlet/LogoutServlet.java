package servlet;

import db.Session;
import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Map;

public class LogoutServlet extends BaseServlet {
    private static final Logger log = LoggerFactory.getLogger(LogoutServlet.class);

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String sessionId = getSessionId(request);
        response.addHeader("Set-Cookie", Session.SESSION_ID + "=" + sessionId + "; Expires=Wednesday, 09-Nov-99 21:00:00 GMT");
        deleteUserAtSession(request);
        response.redirection("/index.html");
    }

    private void deleteUserAtSession(HttpRequest request) {
        String sessionId = getSessionId(request);
        log.debug("[LOGOUT] sessionId = {}", sessionId);
        Session.remove(sessionId);
    }

    private String getSessionId(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        String values = headers.get("Cookie");
        return values.split("; ")[1];
    }

}
