package servlet;

import java.util.Map;

import db.DataBase;
import http.HttpServlet;
import http.HttpStatus;
import http.Request;
import http.Response;
import model.User;

public class UserCreateServlet extends HttpServlet {

    public UserCreateServlet(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Response doPost() {
        Map<String, String> queryParameter = request.getParameters();

        User user = new User(
            queryParameter.get("userId"),
            queryParameter.get("password"),
            queryParameter.get("name"),
            queryParameter.get("email")
        );
        DataBase.addUser(user);
        response.setHttpStatus(HttpStatus.OK);
        return response;
    }
}
