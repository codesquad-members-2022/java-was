package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
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
            sendHttpResponse(out, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHttpResponse(OutputStream out, HttpResponse httpResponse) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        Optional<byte[]> responseBody = httpResponse.getResponseBody();

        if (responseBody.isEmpty()) {
            writeHeaders(dos, httpResponse);
            dos.flush();
            return;
        }
        writeHeaders(dos, httpResponse);
        responseBody(dos, responseBody.get());
    }

    private void writeHeaders(DataOutputStream dos, HttpResponse response) {
        try {
            dos.writeBytes(response.headers());
            dos.writeBytes(response.bodyLength() + "\r\n");
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
