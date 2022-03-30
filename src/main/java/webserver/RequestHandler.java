package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.MyController;
import webserver.controller.SignUpController;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final Map<String, MyController> controllerMap;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        controllerMap = new HashMap<>();
        controllerMap.put("/user/create", new SignUpController());
    }

    public void run() {
//        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            MyHttpRequest myHttpRequest = new MyHttpRequest(in);
            MyHttpResponse myHttpResponse = null;  // TODO : 생성자 구현

            String requestURI = myHttpRequest.getRequestURI();
            log.info("requestURI={}", requestURI);
            String viewName = null;
            MyController myController = controllerMap.get(requestURI);
            if (myController != null) {
                log.info("find controller");
                viewName = myController.process(myHttpRequest, myHttpResponse);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + (viewName == null ? requestURI : ("/" + viewName + ".html"))).toPath());
            response200Header(dos, body.length, myHttpRequest);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, MyHttpRequest request) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + request.getHeader("Accept")[0] + "\r\n");
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
