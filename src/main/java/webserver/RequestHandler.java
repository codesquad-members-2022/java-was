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

import model.User;
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
            BufferedReader buf = new BufferedReader(new InputStreamReader(in));
            String httpHeader = urlDecoding(buf);
            String[] token = parseUrl(httpHeader);
            User user = createUser(separateUserInfo(token));
            log.debug("user={}", user);
            log.debug("Http httpHeaderLine={}", httpHeader);
            readHeader(buf, httpHeader);
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + token[1]).toPath());
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

    private String[] parseUrl(String httpHeader) {
        return HttpRequestUtils.separateUrl(httpHeader);
    }

    private User createUser(Map<String, String> urlParseResult) {
        return new User(urlParseResult.get("userId"),
                urlParseResult.get("password"),
                urlParseResult.get("name"),
                urlParseResult.get("email")
        );
    }

    private Map<String, String> separateUserInfo(String[] token) {
        Map<String, String> urlParseResult = HttpRequestUtils.parseQueryString(token[1]);
        log.debug("httpHeaderLine={}", token[1]);
        return urlParseResult;
    }

    private String urlDecoding(BufferedReader buf) throws IOException {
        return URLDecoder.decode(buf.readLine(), StandardCharsets.UTF_8);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
