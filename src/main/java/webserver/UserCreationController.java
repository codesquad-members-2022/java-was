package webserver;

import db.UserDataBase;
import java.util.Map;
import model.User;

public class UserCreationController extends Controller  {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        User user = new User(httpRequest.getParameter("userId"),
            httpRequest.getParameter("password"),
            httpRequest.getParameter("name"),
            httpRequest.getParameter("email"));

        UserDataBase.add(user);

        httpResponse.redirectTo("/");
    }
}
