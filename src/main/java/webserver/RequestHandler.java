package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final int METHOD = 0;
    private final int URL = 1;
    private final int HTTP_VERSION = 2;
    private final int KEY = 0;
    private final int VALUE = 1;

    private Socket connection;
    private String httpMethod;
    private String requestUrl;
    private String httpVersion;
    private Map<String, String> requestHeaderField;
    private Map<String, String> requestBody;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.requestHeaderField = new HashMap<>();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
             DataOutputStream dos = new DataOutputStream(out)) {
            
            setRequestLine(br);
            setRequestHeader(br);
            setRequestBody(br);

            // 1) 정적 페이지 요청
            if (httpMethod.equals("GET") && requestUrl.endsWith(".html")) {
                responseStaticPage(dos);
            }
            
            // 2) 유저 회원가입 요청
            if (httpMethod.equals("POST") && requestUrl.startsWith("/user/create")) {
                signUpUser(dos);
            }

            // 3) 유저 로그인 요청
            if (httpMethod.equals("POST") && requestUrl.startsWith("/user/login")) {
                signInUser(dos);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void signInUser(DataOutputStream dos) {
        String userId = requestBody.get("userId");
        String password = requestBody.get("password");
        User user = DataBase.findUserById(userId);
        byte[] body = "".getBytes();
        if (user == null || !user.isCorrectPassword(password)) {
            log.debug("로그인을 실패하였습니다!");
            response302Header(dos, "/user/login_failed.html", body.length);
        }
        if (user.isCorrectPassword(password)) {
            log.debug("로그인을 성공했습니다!");
            response302HeaderWithCookie(dos, "/index.html", body.length);
        }
        responseBody(dos, body);
    }

    private void responseStaticPage(DataOutputStream dos) throws IOException {
        byte[] body;
        body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void signUpUser(DataOutputStream dos) throws IOException {
        byte[] body = "".getBytes();

        DataBase.findUserById(requestBody.get("userId"))
                .ifPresentOrElse(user -> {
                    log.debug("중복된 아이디가 존재합니다.");
                    response302Header(dos, "/user/form.html", body.length);
                    responseBody(dos, body);
                }, () -> {
                    User user = new User(
                            requestBody.get("userId"),
                            requestBody.get("password"),
                            requestBody.get("name"),
                            requestBody.get("email"));
                    log.debug("User : {}", user);
                    DataBase.addUser(user);
                    response302Header(dos, "/index.html", body.length);
                    responseBody(dos, body);
                });
    }

    private void setRequestLine(BufferedReader br) throws IOException {
        String requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        log.debug("<<<<<request start>>>>>");
        log.debug("[request line] : {}", requestLine);

        String[] requestInfo = HttpRequestUtils.getRequestInfo(requestLine);
        httpMethod = requestInfo[METHOD];
        requestUrl = requestInfo[URL];
        httpVersion = requestInfo[HTTP_VERSION];
    }

    private void setRequestHeader(BufferedReader br) throws IOException {
        String line;
        while (!"".equals(line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8))) {
            if (line == null) {
                return;
            }
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            requestHeaderField.put(pair.getKey(), pair.getValue());
        }

        requestHeaderField.entrySet().forEach(e -> {
            log.debug("{} : {}", e.getKey(), e.getValue());
        });
        log.debug("<<<<<request end>>>>>");
    }

    private void setRequestBody(BufferedReader br) throws IOException {
        int contentLength = (requestHeaderField.get("Content-Length") == null) ?
                0 : Integer.parseInt(requestHeaderField.get("Content-Length"));
        String data = IOUtils.readData(br, contentLength);
        String decodedData = URLDecoder.decode(data, StandardCharsets.UTF_8);
        requestBody = HttpRequestUtils.parseRequestBody(decodedData);
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

    private void response302Header(DataOutputStream dos, String redirectUrl, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String redirectUrl, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("Set-Cookie: logined=true; Path=/");
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
