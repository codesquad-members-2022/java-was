package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

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

            String requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
            String requestMethod = HttpRequestUtils.getRequestMethod(requestLine);
            String targetURI = HttpRequestUtils.getRequestURI(requestLine);
            String targetPath = HttpRequestUtils.getRequestPath(targetURI);

            String requestHeaders = IOUtils.readHeaders(br);
            Map<String, String> headers = HttpRequestUtils.parseHeaders(requestHeaders);

            if (targetPath.equals("/user/create") && requestMethod.equals("POST")) {
                String requestBody = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                requestBody = URLDecoder.decode(requestBody, StandardCharsets.UTF_8);
                Map<String, String> userCreationForm = HttpRequestUtils.parseQueryString(requestBody);
                processUserCreation(dos, userCreationForm);
                return;
            }

            byte[] body = Files.readAllBytes(Path.of(WEBAPP + targetPath));

            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void processUserCreation(DataOutputStream dos,  Map<String, String> userCreationForm) {
        User user = new User(userCreationForm.get("userId"),
                userCreationForm.get("password"),
                userCreationForm.get("name"),
                userCreationForm.get("email"));
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
