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
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body;

        if (path.equals("/user/create") && method.isPost()) {
            User user = new User(request.getParameter("userId"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("email"));
            log.debug("user created : {}", user);
            setResponseCode302(dos);
            setRedirectLocation(dos, "/index.html");
            dos.flush();
            return;
        }
        body = Files.readAllBytes(new File("./webapp" + path).toPath());
        setResponseCode200(dos);
        setResponseHtml(dos, body.length);
        responseBody(dos, body);
    }

    private void setRedirectLocation(DataOutputStream dos, String redirectLocation) throws IOException {
        dos.writeBytes("location: " + redirectLocation + "\r\n");
    }

    private void setResponseCode200(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
    }

    private void setResponseCode302(DataOutputStream dos) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
    }

    private void setResponseHtml(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

}
