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
import javax.xml.crypto.Data;
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
            DataOutputStream dos = new DataOutputStream(out);
            MyHttpRequest myHttpRequest = new MyHttpRequest(in);

            if (isStaticResponse(myHttpRequest)) {
                responseStaticRequest(myHttpRequest, dos);
                return;
            }

            String requestURI = myHttpRequest.getRequestURI();
            log.info("requestURI={}", requestURI);
            String viewName = null;
            MyController myController = controllerMap.get(requestURI);
            if (myController != null) {
                log.info("find controller");
                viewName = myController.process(myHttpRequest);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            byte[] body = Files.readAllBytes(new File("./webapp" + "/" + viewName + ".html").toPath());
            response200Header(dos, body.length, myHttpRequest);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private boolean isStaticResponse(MyHttpRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.indexOf('.') != -1) {
            return true;
        }
        return false;
    }

    private void responseStaticRequest(MyHttpRequest request, DataOutputStream dos) throws IOException {
        File file = new File("./webapp" + request.getRequestURI());
        byte[] body = Files.readAllBytes(file.toPath());
        response200Header(dos, body.length, request);
        responseBody(dos, body);
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
