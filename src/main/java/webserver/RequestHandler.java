package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.request.MyHttpMethod;
import web.request.MyHttpRequest;
import web.response.MyHttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
            MyHttpResponse response = new MyHttpResponse(out);
            sendResponse(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendResponse(MyHttpRequest request, MyHttpResponse response) throws IOException {
        String path = request.getPath();
        MyHttpMethod method = request.getMethod();
        if (path.equals("/user/create") && method.isPost()) {
            User user = new User(request.getParameter("userId"),
                    request.getParameter("password"),
                    request.getParameter("name"),
                    request.getParameter("email"));
            log.debug("user created : {}", user);
            response.redirectTo("/index.html");
            return;
        }
        response.responseStaticResource(path);
    }
}
