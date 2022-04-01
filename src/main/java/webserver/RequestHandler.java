package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import controller.*;
import http.Request;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<Pair, Controller> controllerMapper;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

     static {
        controllerMapper = new HashMap<>();
        controllerMapper.put(new Pair("POST", "/user/create"), SignUpController.getInstance());
        controllerMapper.put(new Pair("POST", "/user/login"), LogInController.getInstance());
        controllerMapper.put(new Pair("GET", "/user/logout"), LogOutController.getInstance());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            Controller controller = controllerMapper.get(request.methodUrl()) == null ?
                    DefaultController.getInstance() : controllerMapper.get(request.methodUrl());
            Response response = controller.run(request);
            DataOutputStream dataOutputStream = new DataOutputStream(out);
            dataOutputStream.write(response.responseMessage().getBytes(StandardCharsets.UTF_8));
            dataOutputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
