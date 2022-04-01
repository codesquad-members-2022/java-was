package webserver;

import db.UserDataBase;
import java.util.Collection;
import model.User;

public class UserListController extends Controller {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLoggedIn()) {
            httpResponse.redirectTo("/user/login.html");
            return;
        }

        StringBuilder userListBuilder = new StringBuilder();
        Collection<User> users = UserDataBase.findAll();
        userListBuilder.append("<table border='2'>")
            .append("<tr>")
            .append("<th>User Id</th>")
            .append("<th>Name</th>")
            .append("<th>Email</th>")
            .append("</tr>");

        for (User user : users) {
            userListBuilder.append("<tr>")
                .append("<td>").append(user.getUserId()).append("</td>")
                .append("<td>").append(user.getName()).append("</td>")
                .append("<td>").append(user.getEmail()).append("</td>")
                .append("</tr>");
        }

        userListBuilder.append("</table>");

        byte[] body = userListBuilder.toString().getBytes();

        httpResponse.addHeader("Content-Type", ContentTypeMapping.getContentType(".html"));
        httpResponse.addHeader("Content-Length", "" + body.length);
        httpResponse.sendHeader();
        httpResponse.sendBody(body);
    }
}
