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
            String bodyString = getBodyString();
            byte[] newBody = addUserInfoAtBody(users, bodyString);
            response.forward(newBody);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] addUserInfoAtBody(Collection<User> users, String bodyString) {
        int startIndex = bodyString.indexOf("<tbody>");
        int endIndex = bodyString.indexOf("</tbody>");

        StringBuilder sb = new StringBuilder(bodyString.substring(0, startIndex + 7));
        int count = 1;
        for (User user : users) {
            sb.append("<tr>")
                    .append("<th scope=\"row\">" + count++ + "</th>")
                    .append("<td>" + user.getUserId() + "</td>")
                    .append("<td>" + user.getName() + "</td>")
                    .append("<td>" + user.getEmail() + "</ㅌd>")
                    .append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>")
                    .append("</tr>");
        }
        sb.append(bodyString.substring(endIndex));
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private String getBodyString() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/list.html").toPath());
        return new String(body, "UTF-8");
    }
}
