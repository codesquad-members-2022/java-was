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
import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import util.PrintUtils;
import util.Request;

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
                PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

                byte[] body = Files.readAllBytes(new File("./webapp" + request.getPath()).toPath());

                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {

                String pathURL = request.getPath();

                System.out.println("body:" + request.getRequestBody());

//                User user = new User(
//                    parsedQueryString.get("userId"),
//                    parsedQueryString.get("password"),
//                    parsedQueryString.get("name"),
//                    parsedQueryString.get("email")
//                );

                PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

                byte[] body = Files.readAllBytes(new File("./webapp" + pathURL).toPath());

                response200Header(dos, body.length);
                responseBody(dos, body);
            }
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
