package webserver;

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
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream();) {
            DataOutputStream dos = new DataOutputStream(out);
            HttpRequest request = new HttpRequest(in);

            if (request.getHttpMethod().equals("POST")) {
                Map<String, String> userInfo = request.getParams();
                try {
                    DataBase.addUser(User.from(userInfo));
                    sendPostResponse(dos, HttpStatus.REDIRECT);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    sendPostResponse(dos, HttpStatus.BAD_REQUEST);
                }
            } else {
                sendGetResponse(dos, request.getPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPostResponse(DataOutputStream dos, HttpStatus httpStatus) throws IOException {
        HttpResponse response = new HttpResponse(httpStatus, dos);
        if (httpStatus.getStatusCode() == 302) {
            response.setHeader("Location", "/index.html");
        }
        response.send();
    }

    private void sendGetResponse(DataOutputStream dos, String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        HttpResponse response = new HttpResponse(HttpStatus.OK, dos);

        if (path.endsWith("html")) {
            response.setHeader("Content-Type", ContentType.HTML.getType());
        } else if (path.endsWith("css")) {
            response.setHeader("Content-Type", ContentType.CSS.getType());
        } else if (path.endsWith("js")) {
            response.setHeader("Content-Type", ContentType.JS.getType());
        }
        response.sendWithBody(body);
    }
}
