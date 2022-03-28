package servlet;

import http.HttpServlet;
import http.Request;
import http.Response;
import http.Session;

public class LogoutServlet extends HttpServlet {

    public LogoutServlet(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Response doGet() {
        response.setRedirectUrl("/index.html");

        String sessionId = request.getSessionId();

        if (sessionId != null && Session.isSessionIdExist(sessionId)) {
            Session.invalidateSession(sessionId);
            response.addHeader("Set-Cookie", String.format("sessionId=%s; Max-Age=0; Path=/", sessionId));
        }

        return response;
    }
}
