package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final int METHOD = 0;
    private final int URL = 1;
    private final int HTTP_VERSION = 2;
    private final int KEY = 0;
    private final int VALUE = 1;

    private Socket connection;
    private String requestLine;
    private Map<String, String> requestHeaderField;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.requestHeaderField = new HashMap<>();
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
            String[] requestInfo = HttpRequestUtils.getRequestInfo(requestLine);
            String url = requestInfo[URL];
            String queryString = HttpRequestUtils.getQueryString(url);
            Map<String, String> queryParameters = HttpRequestUtils.parseQueryString(queryString);
            User user = new User(queryParameters.get("userId"),
                    queryParameters.get("password"),
                    queryParameters.get("name"),
                    queryParameters.get("email"));
            log.debug("User : {}", user);
            DataBase.addUser(user);
            setRequestHeader(br);

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
