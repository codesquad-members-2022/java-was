package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            RequestReader requestReader = new RequestReader(br);
            Request request = requestReader.getRequest();

            UserManager userManager = new UserManager(request);
            String sessionId = userManager.action();

            // Response Message
            DataOutputStream dos = new DataOutputStream(out);
            Response response = new Response(request.getRequestLine(), sessionId);
            sendResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) throws IOException {
        response.action();

        String header = response.getHeaders();
        dos.writeBytes(header);

        byte[] body = response.getBody();
        if (body != null) {
            dos.write(body, 0, body.length);
        }
    }
}
