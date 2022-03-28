package webserver;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import servlet.CreateUserServlet;
import servlet.LoginServlet;
import servlet.Servlet;
import util.RequestParser;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, Servlet> servletMap = new ConcurrentHashMap<>();

    static {
        servletMap.put("/user/create", new CreateUserServlet());
        servletMap.put("/user/login", new LoginServlet());
    }

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
        if (servletMap.containsKey(path)) {
            Servlet servlet = servletMap.get(path);
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
