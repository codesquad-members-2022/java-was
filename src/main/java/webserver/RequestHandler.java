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
            MyHttpRequest myHttpRequest = new MyHttpRequest(in);
            MyHttpResponse myHttpResponse = new MyHttpResponse(out, myHttpRequest);

            if (myHttpRequest.isStaticRequest()) {
                myHttpResponse.sendStaticResponse();
                return;
            }

            String requestURI = myHttpRequest.getRequestURI();

            MyController myController = controllerMap.get(requestURI);
            if (myController == null) {
                log.info("cannot find controller, URL : {}", requestURI);
                myHttpResponse.send404page();
                return;
            }

            String viewName = myController.process(myHttpRequest);
            myHttpResponse.setViewName(viewName);
            myHttpResponse.sendResponse();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
