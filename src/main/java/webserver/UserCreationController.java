package webserver;

import db.UserDataBase;
import java.util.Map;
import model.User;

public class UserCreationController extends Controller  {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        Map<String, String> userCreationForm = httpRequest.getParameters();
        User user = new User(userCreationForm.get("userId"),
            userCreationForm.get("password"),
            userCreationForm.get("name"),
            userCreationForm.get("email"));

        UserDataBase.add(user);

        httpResponse.response302Header("/");
    }
}
