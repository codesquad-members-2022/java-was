package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            HttpRequest httpRequest = new HttpRequest();
            httpRequest.write(bufferedReader);

            HttpResponse httpResponse = UrlMapper.getResponse(httpRequest, bufferedReader);

            DataOutputStream dos = new DataOutputStream(out);
            Optional<byte[]> response = httpResponse.getResponseBody();

            if (response.isEmpty()) {
                writeHeaders(dos, 0, httpResponse);
                dos.flush();
                return;
            }
            byte[] responseBody = response.get();
            writeHeaders(dos, responseBody.length, httpResponse);
            responseBody(dos, responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void writeHeaders(DataOutputStream dos, int lengthOfBodyContent, HttpResponse response) {
        try {
            dos.writeBytes(String.format("%s %d %s %s",
                response.getVersion(), response.getHttpStatusCode(), response.getHttpStatusMessage(), System.lineSeparator()));
            Map<String, String> responseHeaders = response.getResponseHeaders();

            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                dos.writeBytes(String.format("%s: %s %s", entry.getKey(), entry.getValue(), System.lineSeparator()));
            }

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
