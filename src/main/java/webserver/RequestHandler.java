package webserver;

import controller.FrontController;
import db.DataBase;
import model.ContentType;
import model.HttpStatus;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final FrontController frontController = FrontController.getInstance();
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             DataOutputStream dos = new DataOutputStream(out)) {

            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(dos);

            frontController.handleRequest(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
