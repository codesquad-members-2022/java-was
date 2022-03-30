package webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import webserver.controller.Controller;
import webserver.controller.DefaultController;
import webserver.controller.UserJoinController;
import webserver.controller.UserLoginController;
import webserver.controller.UserLogoutController;
import webserver.http.Request;
import webserver.http.Response;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final Map<Pair, Controller> requestLineHandler = new HashMap<>(
        Map.of(
            new Pair("POST", "/user/create"), new UserJoinController(),
            new Pair("POST", "/user/login"), new UserLoginController(),
            new Pair("GET", "/user/logout"), new UserLogoutController()

        ));
    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            Request request = new Request(in);
            Response response = new Response(out);

            Pair pair = HttpRequestUtils.getKeyValue(request.getRequestLine(), " ");

            Controller controller = Optional.ofNullable(requestLineHandler.get(pair))
                .orElseGet(DefaultController::new);

            controller.process(request, response);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
