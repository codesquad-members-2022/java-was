package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;

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
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String httpHeader = buf.readLine();
            String token = parseUrl(httpHeader);
            DataOutputStream dos = new DataOutputStream(out);
            if (httpHeader.contains("POST")) {
                int length = 0;
                while (!httpHeader.equals("")) {
                    httpHeader = buf.readLine();
                    if (httpHeader.contains("Content-Length")) {
                        String contentLength = httpHeader.split(" ")[1];
                        length = Integer.parseInt(contentLength);
                    }
                }
                String url = urlDecoding(IOUtils.readData(buf, length));
                User user = createUser(separateUserInfo(url));
                DataBase.addUser(user);
                log.debug("user={}", user);
                response302Header(dos);
            }
            log.debug("Http httpHeaderLine={}", httpHeader);
            readHeader(buf, httpHeader);
            byte[] body = Files.readAllBytes(new File("./webapp" + token).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void readHeader(BufferedReader buf, String httpHeader) throws IOException {
        while (!httpHeader.equals("")) {
            httpHeader = buf.readLine();
            log.debug("Http header={}", httpHeader);
        }
    }

    private String parseUrl(String httpHeader) {
        return HttpRequestUtils.separateUrl(httpHeader);
    }

    private User createUser(Map<String, String> urlParseResult) {
        return new User(urlParseResult.get("userId"),
                urlParseResult.get("password"),
                urlParseResult.get("name"),
                urlParseResult.get("email")
        );
    }

    private Map<String, String> separateUserInfo(String token) {
        Map<String, String> urlParseResult = HttpRequestUtils.parseQueryString(token);
        log.debug("httpHeaderLine={}", token);
        return urlParseResult;
    }

    private String urlDecoding(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html \r\n");
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
