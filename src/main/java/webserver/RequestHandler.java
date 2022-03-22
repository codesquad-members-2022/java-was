package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

import db.Sessions;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP = "./webapp";

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = HttpRequest.receive(br);

            if (httpRequest.getCookies() != null) {
                log.debug("httpRequest.getCookies() = " + httpRequest.getCookies());
            }

            if (httpRequest.hasPathEqualTo("/user/create") && httpRequest.hasMethodEqualTo("POST")) {
                processUserCreation(dos, httpRequest.getParameters());
                return;
            }

            if (httpRequest.hasPathEqualTo("/user/login") && httpRequest.hasMethodEqualTo("POST")) {
                processUserLogin(dos, httpRequest.getParameters());
                return;
            }

            byte[] body = Files.readAllBytes(Path.of(WEBAPP + httpRequest.getPath()));

            response200Header(dos, body.length);
            responseBody(dos, body);

            connection.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processUserLogin(DataOutputStream dos, Map<String, String> loginForm) {
        String userId = loginForm.get("userId");
        String password = loginForm.get("password");

        if (DataBase.matchesExistingUser(userId, password)) {
            String sessionId = UUID.randomUUID().toString();
            Sessions.getSession(sessionId)
                    .setAttribute("user", DataBase.findUserById(userId));
            response302HeaderAfterLogin(dos, "/index.html", sessionId);
        }

        response302Header(dos, "/login_failed.html");
    }

    private void processUserCreation(DataOutputStream dos,  Map<String, String> userCreationForm) {
        User user = new User(userCreationForm.get("userId"),
                userCreationForm.get("password"),
                userCreationForm.get("name"),
                userCreationForm.get("email"));
        DataBase.addUser(user);
        log.debug("New User has been created: {}", user);

        response302Header(dos, "/index.html");
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectPath) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderAfterLogin(DataOutputStream dos, String redirectPath, String sessionId) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectPath + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: sessionId=" + sessionId + "; Path=/\r\n");
            dos.writeBytes("Set-Cookie: logged_in=true; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
