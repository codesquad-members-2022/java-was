package webserver;

import db.DataBase;
import java.util.Collection;
import model.User;

public class UserListController extends Controller {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLoggedIn()) {
            httpResponse.response302Header("/user/login.html");
            return;
        }

        StringBuilder sb = new StringBuilder();
        Collection<User> users = DataBase.findAll();
        sb.append("<table border='2'>")
            .append("<tr>")
            .append("<th>User Id</th>")
            .append("<th>Name</th>")
            .append("<th>Email</th>")
            .append("</tr>");

        for (User user : users) {
            sb.append("<tr>")
                .append("<td>").append(user.getUserId()).append("</td>")
                .append("<td>").append(user.getName()).append("</td>")
                .append("<td>").append(user.getEmail()).append("</td>")
                .append("</tr>");
        }

        sb.append("</table");

        byte[] body = sb.toString().getBytes();

        httpResponse.response200Header(body.length, ContentTypeMapping.getContentType(".html"));
        httpResponse.responseBody(body);
    }
}
