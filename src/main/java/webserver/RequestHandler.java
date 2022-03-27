package webserver;

import static util.HttpRequestUtils.parseQueryString;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpMessage httpMessage = new HttpMessage();
            httpMessage.write(bufferedReader);

            //TODO header의 첫번째 라인에서 path 추출 : GET /index.html HTTP/1.1
            /*  GET
                default 경로 요청  -> default path 반환  = file path
                html 등 파일 요청   -> 해당 file 반환     = file path
                ?로 쿼리스트링 요청   -> 201 Created
             */
            if (httpMessage.getMapping()) {
                String path = httpMessage.getPath();
                DataOutputStream dos = new DataOutputStream(out);
                if (path.startsWith("/user/create")) {
                    Map<String, String> params = httpMessage.getQueryString();
                    User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                    log.debug("register new user: {}", user);
                    response201Header(dos); //  201 Created
                    return;
                }
                byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
                response200Header(dos, body.length);
                responseBody(dos, body); responseBody(dos, body);
                return;
            }

            if (httpMessage.postMapping()) {
                Map<String, String> params = httpMessage.getBody(bufferedReader);
                User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                log.debug("post body : {}", user);

                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos, "http://localhost:8080/index.html");
                return;
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response201Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 201 Created \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectedUrl) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + redirectedUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
