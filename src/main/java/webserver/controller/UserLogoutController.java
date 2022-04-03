package webserver.controller;

import java.io.IOException;
import java.util.Optional;

import webserver.http.Request;
import webserver.http.Response;

public class UserLogoutController implements Controller {
    //Set-Cookie: se
    //  Set-Cookie: lang=; Expires=Sun, 06 Nov 1994 08:49:37 GMT
    @Override
    public void process(Request request, Response response) throws IOException {
        Optional.ofNullable(request.getRequestHeader().get("Cookie"))
            .ifPresentOrElse(
                value -> response.response302HeaderLogout("/index.html", "sessionId=" + value),
                () -> response.response302Header("/index.html")
            );
    }
}
