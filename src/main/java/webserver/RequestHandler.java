package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.RequestParser;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String HOME = "/index.html";
    public static final String USER_REGISTRY_FORM = "/user/form.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = buildRequest(in);
            HttpResponse response = buildResponse(out);
            controlResourcePath(request, response); // TODO 이름 다시 생각해보기
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void controlResourcePath(HttpRequest request, HttpResponse response) throws IOException {
        String resourcePath = request.getPath();

        if (resourcePath.startsWith("/user/create")) {
            User user = createUser(request.getBody());
            String nextPath = HOME;

            try {
                DataBase.addUser(user);
            } catch (RuntimeException e) {
                nextPath = USER_REGISTRY_FORM;
            }

            log.debug("[USER] : {}", user);
            response.redirection(nextPath);
            return;
        }

        response.forward(resourcePath);
    }

    private HttpRequest buildRequest(InputStream in) throws IOException {
        return new RequestParser(in).createRequest();
    }

    private HttpResponse buildResponse(OutputStream out) {
        return new HttpResponse(out);
    }

    private User createUser(String resourcePath) throws UnsupportedEncodingException {
        String queryString = HttpRequestUtils.getQueryString(resourcePath);
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        return new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
    }
}
