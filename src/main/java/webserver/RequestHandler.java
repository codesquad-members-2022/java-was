package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

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

            Request request = new Request(br);

            // URL init
            URL url = request.getURL();

            // user save
            if (request.checkPostMethod() && url.comparePath("/user/create")) {
                String messageBody = request.getMessageBody();
                userSave(messageBody, url);
            }

            // Response Message
            DataOutputStream dos = new DataOutputStream(out);
            Response response = new Response(request.getRequestLine(), url, dos);
            response.action();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void userSave(String messageBody, URL url) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(messageBody);
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
            userInfo.get("email"));
        try {
            DataBase.addUser(user);
            url.setRedirectHomePage();
        } catch (IllegalArgumentException e) {
            url.setRedirectSignUpPAGE();
        }
    }
}
