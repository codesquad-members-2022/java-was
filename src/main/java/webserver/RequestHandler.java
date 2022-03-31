package webserver;

import db.DataBase;
import db.SessionDataBase;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest(br);
            HttpResponse httpResponse = new HttpResponse(out);

            if (httpRequest.getPath().equals("/user/create")) {
                User user = new User(
                    httpRequest.getParameter("userId"),
                    httpRequest.getParameter("password"),
                    httpRequest.getParameter("name"),
                    httpRequest.getParameter("email")
                );

                try {
                    DataBase.addUser(user);
                    httpResponse.response302Header("/index.html");
                } catch (IllegalArgumentException e) {
                    log.debug("exception: {}", e.getMessage());
                    httpResponse.response302Header("/user/form.html");
                }
                return;
            }

            if (httpRequest.getPath().equals("/user/login")) {
                User user = DataBase.findUserById(httpRequest.getParameter("userId"));
                if (user == null) {
                    httpResponse.response302Header("/user/login_failed.html");
                    return;
                }
                if (!user.getPassword().equals(httpRequest.getParameter("password"))) {
                    httpResponse.response302Header("/user/login_failed.html");
                    return;
                }
                String sessionId = UUID.randomUUID().toString();
                log.debug("return cookie: {}", sessionId);
                SessionDataBase.save(sessionId, user.getUserId());
                httpResponse.response302WithCookieHeader("/index.html", sessionId);
                return;
            }

            if (httpRequest.getPath().equals("/user/logout")) {
                Map<String, String> cookies = HttpRequestUtils.parseCookies(
                    httpRequest.getHeader("Cookie"));
                String sessionId = cookies.get("sessionId");
                log.debug("sessionId = {}", sessionId);
                if (sessionId == null) {
                    httpResponse.response302Header("/index.html");
                    return;
                }
                httpResponse.response302WithExpiredCookieHeader("/index.html", sessionId);
                SessionDataBase.remove(sessionId);
                return;
            }

            httpResponse.writeBody(httpRequest.getPath());
            httpResponse.response200Header();
            httpResponse.responseBody();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
