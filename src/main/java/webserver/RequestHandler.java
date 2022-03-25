package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.request.MyHttpMethod;
import web.request.MyHttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

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
            MyHttpRequest request = new MyHttpRequest(in);
            sendResponse(request, out);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(MyHttpRequest request, OutputStream out) throws IOException {
        String path = request.getPath();
        MyHttpMethod method = request.getMethod();
        if(path.equals("/user/create") && method.isPost()) {
            User user = new User(request.getParameter("userId"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("email"));
            log.debug("user created : {}",user);
        }

        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }


    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
