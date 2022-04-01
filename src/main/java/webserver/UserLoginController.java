package webserver;

import db.UserDataBase;

public class UserLoginController extends Controller {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        String userId = httpRequest.getParameter("userId");
        String password = httpRequest.getParameter("password");

        if (UserDataBase.matchesExistingUser(userId, password)) {
            httpRequest.getSession()
                    .setAttribute("user", UserDataBase.findById(userId));

            String sessionId = httpRequest.getCookie("sessionId");
            httpResponse.addHeader("Set-Cookie", "sessionId=" + sessionId + "; Path=/");
            httpResponse.redirectTo("/");
        }

        httpResponse.redirectTo("/user/login_failed.html");
    }
}
