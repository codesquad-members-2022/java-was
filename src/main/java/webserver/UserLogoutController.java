package webserver;

import db.Sessions;
import java.util.Map;

public class UserLogoutController extends Controller  {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> cookies = httpRequest.getCookies();

        String sessionId = cookies.get("sessionId");

        if (sessionId == null) {
            httpResponse.response302Header("/index.html");
            return;
        }

        Sessions.remove(sessionId);
        httpResponse.response302HeaderAfterLogout("/index.html", sessionId);
    }
}
