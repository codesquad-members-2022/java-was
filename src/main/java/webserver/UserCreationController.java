package webserver;

import db.DataBase;
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

        DataBase.addUser(user);

        httpResponse.response302Header("/index.html");
    }
}
