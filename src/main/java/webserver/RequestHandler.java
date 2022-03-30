package webserver;

import java.io.*;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.controller.FrontController;
import webserver.request.Request;
import webserver.request.RequestReader;
import webserver.response.Response;
import webserver.response.ResponseWriter;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final FrontController frontController;
    private RequestReader requestReader;
    private ResponseWriter responseWriter;

    public RequestHandler(Socket connectionSocket, FrontController frontController) {
        this.connection = connectionSocket;
        this.frontController = frontController;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            requestReader = new RequestReader(in);
            Request request = requestReader.create();

            Response response = frontController.handleRequest(request);
            responseWriter = new ResponseWriter(response, out);
            responseWriter.writeResponse();
          
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
