package webserver;

import db.CommentDataBase;
import util.TemplateUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class IndexController extends Controller {

    private static final int COMMENT_BOARD_SIZE = 10;

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            String template = Files.readString(Path.of(RESOURCE_ROOT + "/index.html"));
            Map<String, Object> model = Map.of("comments", CommentDataBase.findRecent(COMMENT_BOARD_SIZE));

            byte[] body = TemplateUtil.render(template, model).getBytes(StandardCharsets.UTF_8);

            httpResponse.addHeader("Content-Type", ContentTypeMapping.getContentType(".html"));
            httpResponse.addHeader("Content-Length", "" + body.length);
            httpResponse.sendHeader();
            httpResponse.sendBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
