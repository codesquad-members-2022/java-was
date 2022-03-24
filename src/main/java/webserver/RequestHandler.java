package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private RequestReader requestReader;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            requestReader = new RequestReader(in);
            Request request = requestReader.create();
            String url = request.getUrl();

            DataOutputStream dos = new DataOutputStream(out);
            if(request.isSameMethod("POST")) {
                String redirectUrl = executeLogicAndGetRedirectUrl(url, request.getRequestBody());
                log.debug("requestBody : {}", request.getRequestBody());
                response302Header(dos, redirectUrl);
            }

            if(request.isSameMethod("GET")) {
                byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
                if(url.endsWith(".css")) {
                    response200CssHeader(dos, body.length);
                }
                if(url.endsWith(".html")) {
                    response200Header(dos, body.length);
                }
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String executeLogicAndGetRedirectUrl(String url, String requestBody) {
        if(url.equals("/user/create")) {
            return signUp(requestBody);
        }
        return url;
    }

    private String signUp(String requestBody) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(requestBody);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        DataBase.addUser(user);
        log.debug("{} sign up", user);

        return "/index.html";
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

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
            dos.writeBytes("Location: "+ url);
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
