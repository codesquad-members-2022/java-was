package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import com.google.common.base.Strings;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
        connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            DataOutputStream dos = new DataOutputStream(out);
            String headerLine = bufferedReader.readLine();
            String requestLine = headerLine;

            while(!Strings.isNullOrEmpty(headerLine)) {
                log.debug("request header : {}", headerLine);
                headerLine = bufferedReader.readLine();
            }

            String url = HttpRequestUtils.parseUrl(requestLine);
            String queryString = HttpRequestUtils.getQueryString(requestLine);

            url = findView(url, queryString);

            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

            if(url.endsWith(".css")) {
                response200CssHeader(dos, body.length);
            }
            if(url.endsWith(".html")) {
                response200Header(dos, body.length);
            }

            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String findView(String url, String queryString) {
        if (url.equals("/user/create")) {
            return signUp(queryString);
        }

        return url; 
    }

    private String signUp(String queryString) {
        Map<String, String> map = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        DataBase.addUser(user);

        return "/index.html";
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

    private void response200CssHeader(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
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
