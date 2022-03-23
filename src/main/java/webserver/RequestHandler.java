package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

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
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest request = new HttpRequest(br);

            if (request.getHttpMethod().equals("POST")) {
                Map<String, String> userInfo = request.getParams();
                User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
                log.debug("{}", user);

                sendPostResponse(dos);
            } else {
                sendGetResponse(new DataOutputStream(dos), request.getAbsPath());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendPostResponse(DataOutputStream dos) {
        HttpResponse response = new HttpResponse(302, dos);
        try {
            response.writeLocation("/index.html");
            response.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendGetResponse(DataOutputStream dos, String path) {
        try {
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
            HttpResponse response = new HttpResponse(200, dos);
            response.writeBody(body);
            response.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
