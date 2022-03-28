package servlet;

import java.util.Map;

import db.DataBase;
import http.HttpServlet;
import http.Request;
import http.Response;
import http.Session;
import model.User;

public class LoginServlet extends HttpServlet {

    public LoginServlet(Request request, Response response) {
        super(request, response);
    }

    @Override
    public Response doPost() {
        Map<String, String> queryParameter = request.getParameters();
        String userId = queryParameter.get("userId");
        String password = queryParameter.get("password");

        if (DataBase.isUserIdExist(userId)) {
            User user = DataBase.findByUserId(userId);
            if (user.isPassword(password)) {
                String sessionId = Session.createSession();
                Session.setAttribute(sessionId, "userId", userId);
                response.addHeader("Set-Cookie", String.format("sessionId= %s; Path=/", sessionId));
                response.setRedirectUrl("/index.html");
                return response;
            }
        }
        response.setRedirectUrl("/user/login_failed.html");
        return response;
    }

}
