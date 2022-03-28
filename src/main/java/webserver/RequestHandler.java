package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CreateUserServlet;
import servlet.LoginServlet;
import util.RequestParser;

import java.io.*;
import java.net.Socket;

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
            controlServlet(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void controlServlet(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();
        log.debug("[PATH] : {}", path);
        if (request.getPath().equals("/user/create")) {
            CreateUserServlet servlet = new CreateUserServlet();
            servlet.service(request, response);
            return;
        }
        if (request.getPath().equals("/user/login")) {
            LoginServlet servlet = new LoginServlet();
            servlet.service(request, response);
            return;
        }

        response.forward(path);
    }

    private HttpRequest buildRequest(InputStream in) throws IOException {
        return RequestParser.parse(in);
    }

    private HttpResponse buildResponse(OutputStream out) {
        return new HttpResponse(out);
    }
}
