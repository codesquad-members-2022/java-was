package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    private static final String staticResourcePath = System.getProperty("user.dir") + "/webapp";
    private static final int REQUEST_URI_INDEX = 1;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
             OutputStream out = connection.getOutputStream()) {

            // 1. request line 을 읽고 어떤 파일을 요청하는지 파악
            String requestUrl = readRequestUrl(br);

            // 2. header 를 로그로 출력
            printHeaders(br);

            // 3. 1번에서 요청한 파일을 읽어서, String -> byte[] 로 변환하여 브라우저로 반환
            byte[] body = IOUtils.readFile(staticResourcePath + requestUrl);

            String contentType = extractContentType(requestUrl);

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length, contentType);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String extractContentType(String requestUrl) {
        String[] split = requestUrl.split("\\.");
        return ProvidedExtension.extensionResolver(split[split.length - 1]);
    }

    private void printHeaders(BufferedReader br) throws IOException {
        String header;
        while (!(header = br.readLine()).equals("") && header != null) {
            log.debug("header : {}", header);
        }
    }

    private String readRequestUrl(BufferedReader br) throws IOException {
        String[] requestLine = br.readLine().split(" ");
        return requestLine[REQUEST_URI_INDEX];
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes(String.format("Content-Type: %s;charset=utf-8\r\n", contentType));
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
