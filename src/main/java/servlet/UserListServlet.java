package servlet;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;

public class UserListServlet extends BaseServlet {
    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        Collection<User> users = DataBase.findAll();
        try {
            byte[] body = Files.readAllBytes(new File("./webapp/user/list.html").toPath());
            String bodyString = new String(body, "UTF-8");
            int startIndex = bodyString.indexOf("<tbody>");
            StringBuilder sb = new StringBuilder(bodyString.substring(0, startIndex + 7));
            int count = 1;
            for (User user : users) {
                sb.append("<tr>")
                        .append("<th scope=\"row\">" + count++ + "</th>")
                        .append("<td>" + user.getUserId() + "</td>")
                        .append("<td>" + user.getName() + "</td>")
                        .append("<td>" + user.getEmail() + "</td>")
                        .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                        .append("</tr>");
            }
            int closeIndex = bodyString.indexOf("</tbody>");
            sb.append(bodyString.substring(closeIndex));
            byte[] newBody = sb.toString().getBytes(StandardCharsets.UTF_8);
            response.forward2(newBody);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
