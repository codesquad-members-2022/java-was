package webserver;

import db.UserDataBase;
import util.TemplateUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;

public class UserListController extends Controller {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (!httpRequest.isLoggedIn()) {
            httpResponse.redirectTo("/user/login.html");
            return;
        }

        try {
            String template = Files.readString(Path.of(RESOURCE_ROOT + "/user/list.html"));
            Map<String, Object> model = Map.of("users", new ArrayList<>(UserDataBase.findAll()));

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
