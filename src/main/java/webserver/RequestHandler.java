package webserver;

import controller.FirstController;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.PrintUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
            OutputStream out = connection.getOutputStream()) {
            Request request = new Request(in);
            request.readRequest();

            Response response = new Response(out);

            FirstController firstController = FirstController.getInstance();
            firstController.run(request, response);

            PrintUtils.printRequestHeaders(request.getHeaderPairs(), request.getRequestLine());

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
