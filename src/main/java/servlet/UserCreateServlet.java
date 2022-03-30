package servlet;

import java.util.Map;

import db.DataBase;
import http.HttpServlet;
import http.Request;
import http.Response;
import model.User;

public class UserCreateServlet extends HttpServlet {

    @Override
    public Response doPost(Request request, Response response) {
        Map<String, String> queryParameter = request.getParameters();

        User user = new User(
            queryParameter.get("userId"),
            queryParameter.get("password"),
            queryParameter.get("name"),
            queryParameter.get("email")
        );
        if (DataBase.isUserIdExist(user.getUserId())) {
            response.setRedirectUrl("/user/form.html");
            return response;
        }
        DataBase.addUser(user);
        response.setRedirectUrl("/index.html");
        return response;
    }
}
