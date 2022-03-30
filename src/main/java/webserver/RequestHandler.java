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
            OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            request.readRequest();

            Response response = new Response(out, request);
            response.writeResponse();

            PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
