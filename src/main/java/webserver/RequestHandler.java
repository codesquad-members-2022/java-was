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
    public static final String RESET = "\u001B[0m";
    public static final String FONT_RED = "\u001B[31m";
    public static final String FONT_BLUE = "\u001B[34m";

    private Socket connection;
    private String requestLine;
    private String httpMethod;
    private String requestUrl;
    private Map<String, String> requestHeaderField;
    private Map<String, String> requestBody;
    private StringBuilder response = new StringBuilder();

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
                sighUpUser(dos);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        } catch (NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    private void responseStaticPage(DataOutputStream dos) throws IOException {
        byte[] body;
        body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void sighUpUser(DataOutputStream dos) throws IOException {
        byte[] body = "".getBytes();
        User user = new User(
                requestBody.get("userId"),
                requestBody.get("password"),
                requestBody.get("name"),
                requestBody.get("email"));
        log.debug("User : {}", user);
        DataBase.addUser(user);
        response302Header(dos, "/index.html", body.length);
        responseBody(dos, body);
    }

    private void setRequestLine(BufferedReader br) throws IOException {
        requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        log.debug(FONT_RED + "<<<<<request start>>>>>" + RESET);
        log.debug("[request line] : {}", requestLine);

        String[] requestInfo = HttpRequestUtils.getRequestInfo(requestLine);
        httpMethod = requestInfo[METHOD];
        requestUrl = requestInfo[URL];
    }

    private void setRequestHeader(BufferedReader br) throws IOException {
        String line;
        while (!"".equals(line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8))) {
            if (line == null) {
                return;
            }
            String[] header = line.split(": ");
            requestHeaderField.put(header[KEY], header[VALUE]);
        }

        requestHeaderField.entrySet().forEach(e -> {
            log.debug("{} : {}", e.getKey(), e.getValue());
        });
        log.debug(FONT_RED + "<<<<<request end>>>>>" + RESET);
    }

    private void setRequestBody(BufferedReader br) throws IOException {
        String data = IOUtils.readData(br, Integer.parseInt(requestHeaderField.get("Content-Length")));
        String decodedData = URLDecoder.decode(data, StandardCharsets.UTF_8);
        requestBody = HttpRequestUtils.parseRequestBody(decodedData);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            response.append("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            response.append("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.append("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
            response.append("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String redirectUrl, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            response.append("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            response.append("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            response.append("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("Location: " + redirectUrl + "\r\n");
            response.append("Location: " + redirectUrl + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            log.debug(FONT_BLUE + "<<<<<response start>>>>>" + RESET);
            log.debug(response.toString());
            log.debug(FONT_BLUE + "<<<<<response end>>>>>" + RESET);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
