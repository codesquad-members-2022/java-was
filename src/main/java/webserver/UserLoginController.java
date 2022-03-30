package webserver;

import db.DataBase;
import db.Sessions;

import java.util.Map;
import java.util.UUID;

public class UserLoginController extends Controller  {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> loginForm = httpRequest.getParameters();
        Map<String, String> cookies = httpRequest.getCookies();

        String userId = loginForm.get("userId");
        String password = loginForm.get("password");

        if (DataBase.matchesExistingUser(userId, password)) {
            String sessionId = cookies.containsKey("sessionId") ?
                cookies.get("sessionId") : UUID.randomUUID().toString();

            Sessions.getSession(sessionId)
                .setAttribute("user", DataBase.findUserById(userId));

            httpResponse.response302HeaderAfterLogin("/index.html", sessionId);
        }

        httpResponse.response302Header("/user/login_failed.html");
    }
}
