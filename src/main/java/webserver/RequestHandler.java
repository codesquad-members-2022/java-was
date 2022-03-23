package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String WEBAPP_PATH = "./webapp";

    private Socket connection;
    private RequestReader requestReader;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = parseRequest(in);
            Response response = handlePath(request);
            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Request parseRequest(InputStream in) throws IOException {
        requestReader = new RequestReader(in);
        return requestReader.create();
    }

    private void sendResponse(OutputStream out, Response response) {
        ResponseWriter responseWriter = new ResponseWriter(new DataOutputStream(out));
        responseWriter.from(response);
    }

    private Response handlePath(Request request) throws IOException {

        // create
        if (request.getMethod().equals("POST") && request.parsePath().equals("/user/create")) {
            createUser(request);
            return new Response.Builder(Status.FOUND)
                    .addHeader("Location", "http://localhost:8080/index.html")
                    .build();
        }

        // default
        byte[] body = readFile(request);
        return new Response.Builder(Status.OK)
                .addHeader("Content-Type", request.toContentType())
                .addHeader("Content-Length", String.valueOf(body.length))
                .body(body)
                .build();
    }

    private byte[] readFile(Request request) throws IOException {
        return Files.readAllBytes(new File(WEBAPP_PATH + request.parsePath()).toPath());
    }

    private void createUser(Request request) {
        Map<String, String> queryString = request.getQueryString();

        User user = new User(
            queryString.get("userId"),
            queryString.get("password"),
            queryString.get("name"),
            queryString.get("email")
        );
        log.info("user={}", user);
    }

}
