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
import webserver.controller.HomeController;
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
        controllerMap.put("/", new HomeController());
    }

    public void run() {
//        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            MyHttpRequest myHttpRequest = new MyHttpRequest(in);

            if (isStaticRequest(myHttpRequest)) {
                responseStaticRequest(myHttpRequest, dos);
                return;
            }

            String requestURI = myHttpRequest.getRequestURI();
            MyController myController = controllerMap.get(requestURI);
            if (myController == null) {
                log.info("cannot find controller, URL : {}", requestURI);
                send404page(dos, myHttpRequest);
                return;
            }
            String viewName = myController.process(myHttpRequest);


            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            byte[] body = Files.readAllBytes(new File("./webapp" + "/" + viewName + ".html").toPath());
            responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.OK);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void send404page(DataOutputStream dos, MyHttpRequest myHttpRequest) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/error/404.html").toPath());
        responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.NOT_FOUND);
        responseBody(dos, body);
    }

    private boolean isStaticRequest(MyHttpRequest request) {
        String requestURI = request.getRequestURI();
        if (requestURI.indexOf('.') != -1) {
            return true;
        }
        return false;
    }

    private void responseStaticRequest(MyHttpRequest myHttpRequest, DataOutputStream dos) throws IOException {
        File file = new File("./webapp" + myHttpRequest.getRequestURI());
        if (!file.exists()) {
            log.info("cannot find static resource : {}", myHttpRequest.getRequestURI());
            send404page(dos, myHttpRequest);
            return;
        }
        byte[] body = Files.readAllBytes(file.toPath());
        responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.OK);
        responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, MyHttpRequest request, HttpResponseStatusCode statusCode) {
        try {
            dos.writeBytes(statusCode.getValue());
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
