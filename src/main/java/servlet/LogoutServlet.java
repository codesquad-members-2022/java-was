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
    private static final int SESSION_ID_IDX = 1;

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String sessionId = getSessionId(request);
        response.addHeader("Set-Cookie", Session.SESSION_ID + "=deleted; path=/; Max-Age=0; Expires=Wednesday, 09-Nov-99 21:00:00 GMT");
        Session.remove(sessionId);
        response.redirection("/index.html");
    }

    private String getSessionId(HttpRequest request) {
        Map<String, String> headers = request.getHeaders();
        String values = headers.get("Cookie");
        return values.split("; ")[SESSION_ID_IDX];
    }

}
