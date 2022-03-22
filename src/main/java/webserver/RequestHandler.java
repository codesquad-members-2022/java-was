package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.MyController;
import controller.SaveUserController;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Map<String, MyController> handlerMapper = new HashMap<>();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initHandlerMapper();
    }

    private void initHandlerMapper() {
        handlerMapper.put("/user/create", new SaveUserController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            String firstLine = bf.readLine();
            String path = HttpRequestUtils.parsePath(firstLine);

            if (handlerMapper.containsKey(path)) {
                MyController myController = handlerMapper.get(path);

                Map<String, String> paramMap = HttpRequestUtils.parseQueryString(firstLine);
                Map<String, Object> model = new HashMap<>();
                path = myController.process(paramMap, model);
            }



            printRequestHeader(firstLine, bf);

            byte[] responseBody = createResponseBody(path);
            response200Header(dos, responseBody.length);
            responseBody(dos, responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private byte[] createResponseBody(String path) throws IOException {
        if (!path.equals("/")) {
            return Files.readAllBytes(new File("./webapp/" + path).toPath());
        }
        return "Hello World".getBytes(StandardCharsets.UTF_8);
    }

    private void printRequestHeader(String firstLine, BufferedReader bf) throws IOException {
        log.info("Request line: {}", firstLine);
        String line = bf.readLine();
        while (!line.equals("")) {
            log.info("header: {}", line);
            line = bf.readLine();
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
