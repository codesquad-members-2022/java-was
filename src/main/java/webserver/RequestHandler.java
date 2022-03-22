package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            String url = getUrl(in);
            String route = url;
            String queryParameter = null;
            if (url.contains("?")) {
                route = url.split("\\?")[0];
                queryParameter = HttpRequestUtils.parseUrl(url);
            }

            log.debug("route: {}", route);

            if (url.startsWith("/user/create")) {
                createUser(queryParameter);
                route = "/index.html";
            }

            byte[] body = Files.readAllBytes(new File("./webapp" + route).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createUser(String queryString) {
        Map<String, String> params = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
        log.debug(user.toString());
    }

    private String getUrl(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
        String line = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String[] tokens = line.split(" ");
        showHeaders(br);
        return tokens[1];
    }

    private void showHeaders(BufferedReader br) throws IOException {
        String line;
        while (!(line = br.readLine()).equals("")) {
            log.debug("line : {}", line);
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
