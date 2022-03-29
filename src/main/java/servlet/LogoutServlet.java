package servlet;

import http.Cookie;
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
            Cookie sessionIdCookie = new Cookie("sessionId", sessionId);
            sessionIdCookie.setMaxAge(0);
            sessionIdCookie.setPath("/");
            response.addCookie(sessionIdCookie);
        }

        return response;
    }
}
