package webserver;

import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP_PATH = "./webapp";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = parseRequest(in);
            handlePath(request, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Request parseRequest(InputStream in) throws IOException {
        RequestReader requestReader = new RequestReader(in);
        return requestReader.create();
    }

    public void handlePath(Request request, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        // create
        if (request.parsePath().equals("/create")) {
            createUser(request, dos);
            response201Header(dos);
            return;
        }

        // default
        byte[] body = readFile(request, dos);
        response200Header(dos, body.length, request.toContentType());
        responseBody(dos, body);
    }

    private byte[] readFile(Request request, DataOutputStream dos) throws IOException {
        return Files.readAllBytes(new File(WEBAPP_PATH + request.parsePath()).toPath());
    }

    private void createUser(Request request, DataOutputStream dos) {
        Map<String, String> qs = request.parseQueryString();

        User user = new User(
            qs.get("userId"),
            qs.get("password"),
            qs.get("name"),
            qs.get("email")
        );
        log.info("user={}", user);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent,
        String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response201Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 201 CREATED \r\n");
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
