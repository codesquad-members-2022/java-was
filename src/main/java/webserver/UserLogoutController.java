package webserver;

import db.Sessions;

public class UserLogoutController extends Controller  {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        String sessionId = httpRequest.getCookie("sessionId");

        Sessions.remove(sessionId);

        httpResponse.addHeader("Set-Cookie", "sessionId=" + sessionId + "; Max-Age=0; Path=/");
        httpResponse.redirectTo("/");
    }
}
