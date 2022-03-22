package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final int REQUEST_INDEX = 1;
    private static final String WEBAPP_ROOT = "./webapp";
    private static final String BLANK = " ";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String requestUrl = getRequestUrl(bufferedReader);

            printHeaders(bufferedReader);

            makeResponseBody(out, requestUrl);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String getRequestUrl(BufferedReader bufferedReader) throws IOException {
        String[] requestLine = bufferedReader.readLine().split(BLANK);
        return requestLine[REQUEST_INDEX];
    }

    private void printHeaders(BufferedReader bufferedReader) throws IOException {
        String line;
        while (!(line = bufferedReader.readLine()).equals(BLANK)) {
            line = bufferedReader.readLine();
            log.debug("header: {} ", line);
        }
    }

    private void makeResponseBody(OutputStream out, String requestUrl) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] body = Files.readAllBytes(new File(WEBAPP_ROOT + requestUrl).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
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
