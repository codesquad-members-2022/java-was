package webserver;

import db.CommentDataBase;
import model.Comment;
import model.User;

public class CommentCreationController extends Controller {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLoggedIn()) {
            httpResponse.redirectTo("/user/login.html");
            return;
        }

        HttpSession session = httpRequest.getSession();
        User currentUser = (User) session.getAttribute("user");
        String contents = httpRequest.getParameter("comment");

        CommentDataBase.add(new Comment(currentUser.getName(), contents));

        httpResponse.redirectTo("/");
    }
}
