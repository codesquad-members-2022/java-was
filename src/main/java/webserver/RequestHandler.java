package webserver;

import http.HttpRequest;
import http.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.RequestParser;
import util.ResponseBuilder;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    public static final String HOME = "/index.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = buildRequest(in);
            controlResourcePath(request, out); // TODO 이름 다시 생각해보기
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void controlResourcePath(HttpRequest request, OutputStream out) throws IOException {
        String resourcePath = request.getPath();

        if (resourcePath.startsWith("/user/create")) {
            User user = createUser(request.getBody());
            log.debug("[USER] : {}", user);
            //response302Header(dos, HOME);
            HttpResponse response = new HttpResponse.Builder()
                    .status("HTTP/1.1 302 Found")
                    .setHeader("Location", HOME)
                    .build();
            ResponseBuilder responseBuilder = new ResponseBuilder(response, out);
            responseBuilder.sendResponse();
            return;
        }

        byte[] body = viewResolver(resourcePath);
        HttpResponse response = new HttpResponse.Builder()
                .status("HTTP/1.1 200 OK")
                .setHeader("Content-Type", "text/html;charset=utf-8")
                .setHeader("Content-Length", String.valueOf(body.length))
                .body(body)
                .build();

        ResponseBuilder responseBuilder = new ResponseBuilder(response, out);
        responseBuilder.sendResponse();
    }

    private HttpRequest buildRequest(InputStream in) throws IOException {
        return new RequestParser(in).createRequest();
    }

    private String getResourcePath(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        return HttpRequestUtils.getUrlFromRequestLine(requestLine);
    }

    private byte[] viewResolver(String resourcePath) throws IOException {
        return Files.readAllBytes(new File("./webapp" + resourcePath).toPath());
    }

    private User createUser(String resourcePath) throws UnsupportedEncodingException {
        String queryString = HttpRequestUtils.getQueryString(resourcePath);
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        return new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
    }

//    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//        try {
//            dos.writeBytes("HTTP/1.1 200 OK \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    private void response302Header(DataOutputStream dos, String path) {
//        try {
//            dos.writeBytes("HTTP/1.1 302 Found \r\n");
//            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//            dos.writeBytes("Location: " + path + "\r\n");
//            dos.writeBytes("\r\n");
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
//
//    private void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
//            log.error(e.getMessage());
//        }
//    }
}
