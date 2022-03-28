package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader br = new BufferedReader(
                new InputStreamReader(in, StandardCharsets.UTF_8));

            String requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
            String url = HttpRequestUtils.getUrl(requestLine);

            List<Pair> headers = IOUtils.readRequestHeader(br);

            HttpRequest httpRequest = new HttpRequest(requestLine, headers);

            if (httpRequest.getHttpMethod().equals("POST")) {
                int contentLength = 0;
                for (int i = 0; i < headers.size(); i++) {
                    if (headers.get(i).getKey().equals("Content-Length")) {
                        contentLength = Integer.parseInt(headers.get(i).getValue());
                    }
                }

                String queryString = IOUtils.readData(br, contentLength);
                Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
                httpRequest.setParameters(parameters);

                User user = new User(
                    httpRequest.getParameter("userId"),
                    httpRequest.getParameter("password"),
                    httpRequest.getParameter("name"),
                    httpRequest.getParameter("email")
                );

                DataBase.addUser(user);
                DataOutputStream dos = new DataOutputStream(out);
                response302Header(dos);
                dos.flush();
                return;
            }

            byte[] body = IOUtils.readRequestResource(url);
            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> getParameters(String queryString) {
        return HttpRequestUtils.parseQueryString(queryString);
    }

    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: http://localhost:8080/index.html\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
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
