package webserver.controller;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;
import webserver.StatusCode;

public class UserCreateController extends Controller{
    private static final Logger log = LoggerFactory.getLogger(UserCreateController.class);

    public UserCreateController(Request request, Response response) {
        super(request, response);
    }

    @Override
    public void service() {
        User findUser = DataBase.findUserById(request.getParam("userId"));
        if (findUser == null) {
            User user = new User(request.getParam("userId"),
                    request.getParam("password"),
                    request.getParam("name"),
                    request.getParam("email"));
            DataBase.addUser(user);
            log.debug("회원가입완료 {}", user);
            response.setRedirect(StatusCode.REDIRECTION_302,
                    "http://localhost:8080/index.html");
        } else {
            response.setRedirect(StatusCode.REDIRECTION_302,
                    "http://localhost:8080/user/form.html");
        }
    }

}
