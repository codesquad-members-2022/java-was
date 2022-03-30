package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.request.HttpMethod;
import web.request.HttpRequest;
import web.response.HttpResponse;
import web.session.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

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
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            sendResponse(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        HttpMethod method = request.getMethod();
        if (path.equals("/user/create") && method == HttpMethod.POST) {
            userJoin(request, response);
        } else if (path.equals("/user/login") && method == HttpMethod.POST) {
            login(request, response);
        } else {
            response.responseStaticResource(path);
        }
    }

    private void userJoin(HttpRequest request, HttpResponse response) throws IOException {
        User user = new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        try {
            validateDuplicateUser(user);
            DataBase.addUser(user);
            response.redirectTo("/index.html");
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
            response.redirectTo("/user/form.html");
        }
    }

    private void login(HttpRequest request, HttpResponse response) throws IOException {
        String inputUserId = request.getParameter("userId");
        String inputUserPassword = request.getParameter("password");

        try {
            User findUser = matchAndGetUser(inputUserId,inputUserPassword);
            SessionManager.createSession(findUser, response);
            response.responseStaticResource("/index.html");
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            response.redirectTo("/user/login_failed.html");
        }
    }

    private User matchAndGetUser(String inputUserId, String inputUserPassword) {
        User findUser = DataBase.findUserById(inputUserId).orElseThrow(
                () -> new NoSuchElementException("존재하지 않는 회원입니다.")
        );
        findUser.matchPassword(inputUserPassword);
        return findUser;
    }

    private void validateDuplicateUser(User user) {
        DataBase.findUserById(user.getUserId())
                .ifPresent(duplicateUser -> {
                    throw new IllegalStateException("중복된 아이디가 존재합니다.");
                });
    }
}
