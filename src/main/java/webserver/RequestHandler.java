package webserver;

import db.DataBase;
import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.RequestParser;
import util.ResponseBuilder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
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
            controlResourcePath(request, out); // TODO 이름 다시 생각해보기
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void controlResourcePath(HttpRequest request, OutputStream out) throws IOException {
        String resourcePath = request.getPath();
        HttpResponse response = null;

        if (resourcePath.startsWith("/user/create")) {
            User user = createUser(request.getBody());
            String nextPath = HOME;

            try {
                DataBase.addUser(user);
            } catch (RuntimeException e) {
                nextPath = USER_REGISTRY_FORM;
            }

            log.debug("[USER] : {}", user);
            response = status302(nextPath);
            sendResponse(response, out);
            return;
        }

        response = status200(viewResolver(resourcePath));
        sendResponse(response, out);
    }

    private HttpResponse status200(byte[] body) {
        return new HttpResponse.Builder()
                .status("HTTP/1.1 200 OK")
                .setHeader("Content-Type", "text/html;charset=utf-8")
                .setHeader("Content-Length", String.valueOf(body.length))
                .body(body)
                .build();
    }

    private HttpResponse status302(String path) {
        return new HttpResponse.Builder()
                .status("HTTP/1.1 302 Found")
                .setHeader("Location", path)
                .build();
    }

    private void sendResponse(HttpResponse response, OutputStream out) {
        new ResponseBuilder(response, out).sendResponse();
    }

    private HttpRequest buildRequest(InputStream in) throws IOException {
        return new RequestParser(in).createRequest();
    }

    private byte[] viewResolver(String resourcePath) throws IOException {
        return Files.readAllBytes(new File("./webapp" + resourcePath).toPath());
    }

    private User createUser(String resourcePath) throws UnsupportedEncodingException {
        String queryString = HttpRequestUtils.getQueryString(resourcePath);
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        return new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
    }
}
