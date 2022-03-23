package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PrintUtils;
import util.Request;
import util.Response;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream();
            InputStreamReader inputReader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(inputReader)) {

            Request request = new Request();
            request.readRequest(br);
            DataOutputStream dos = new DataOutputStream(out);

            if (request.getHttpMethod().equals("GET")) {
                log.debug("GET 요청, requestLine: {}", request.getRequestLine());
                PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

                byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());
                Response response = new Response(body, dos);
                response.response200Header();
                response.responseBody();

            } else if (request.getHttpMethod().equals("POST") && request.getPath().equals("/user/create")){
                log.debug("POST 요청, requestLine: {}", request.getRequestLine());
                Map<String, String> parsedBody = request.takeParsedBody();
                log.debug("POST BODY: {}", parsedBody);
                User user = new User(
                    parsedBody.get("userId"),
                    parsedBody.get("password"),
                    parsedBody.get("name"),
                    parsedBody.get("email")
                );
                PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

                byte[] body = Files.readAllBytes(new File("./webapp/index.html").toPath());
                Response response = new Response(body, dos);
                response.response302Header("http://localhost:8080/index.html");
                response.responseBody();
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
