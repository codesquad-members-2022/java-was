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

            httpResponse.response302HeaderAfterLogin("/", httpRequest.getCookie("sessionId"));
        }

        httpResponse.response302Header("/user/login_failed.html");
    }
}
