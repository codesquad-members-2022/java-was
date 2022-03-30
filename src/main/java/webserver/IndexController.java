package webserver;

import db.CommentDataBase;
import model.Comment;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IndexController extends Controller {

    private static final String INDEX_HTML = "/index.html";
    private static final String COMMENT_BOARD_TAG_REGEX = "\\{\\{commentBoard}}";
    private static final int COMMENT_BOARD_SIZE = 5;

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            String html = Files.readString(Path.of(RESOURCE_ROOT + INDEX_HTML));
            html = html.replaceFirst(COMMENT_BOARD_TAG_REGEX, createCommentBoard());
            byte[] body = html.getBytes(StandardCharsets.UTF_8);

            httpResponse.response200Header(body.length, ContentTypeMapping.getContentType(".html"));
            httpResponse.responseBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String createCommentBoard() {
        StringBuilder commentBoardBuilder = new StringBuilder();
        List<Comment> comments = CommentDataBase.findRecent(COMMENT_BOARD_SIZE);
        commentBoardBuilder.append("<table border='2' style='margin-left:auto; margin-right:auto'>")
                .append("<tr>")
                .append("<th>작성일시</th>")
                .append("<th>이름</th>")
                .append("<th>내용</th>")
                .append("</tr>");

        for (Comment comment : comments) {
            commentBoardBuilder.append("<tr>")
                    .append("<td>").append(comment.getDateWritten()).append("</td>")
                    .append("<td>").append(comment.getAuthorName()).append("</td>")
                    .append("<td>").append(comment.getContents()).append("</td>")
                    .append("</tr>");
        }

        commentBoardBuilder.append("</table>");

        return commentBoardBuilder.toString();
    }
}
