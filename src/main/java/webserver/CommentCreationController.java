package webserver;

import db.CommentDataBase;
import db.Sessions;
import model.Comment;
import model.User;

public class CommentCreationController extends Controller {

    @Override
    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLoggedIn()) {
            httpResponse.response302Header("/user/login.html");
            return;
        }
        
        String sessionId = httpRequest.getCookies().get("sessionId");
        User currentUser = (User) Sessions.getSession(sessionId).getAttribute("user");
        String contents = httpRequest.getParameters().get("comment");

        CommentDataBase.add(new Comment(currentUser.getName(), contents));

        httpResponse.response302Header("/");
    }
}
