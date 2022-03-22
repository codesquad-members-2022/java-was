package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import config.ProvidedExtension;
import config.RequestMapping;
import http.HttpMethod;
import webserver.dto.RequestLine;

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
            RequestLine requestLine = parseRequestLine(br);

            // 2. 매핑 정보 확인
            if (RequestMapping.contains(requestLine.getHttpMethod(), requestLine.getUrl())) {
                // 구현 필요
                return;
            }

            // 2. header 를 로그로 출력
            printHeaders(br);

            // 3. 1번에서 요청한 파일을 읽어서, String -> byte[] 로 변환하여 브라우저로 반환
            byte[] body = IOUtils.readFile(staticResourcePath + requestUrl);

            String contentType = extractContentType(requestUrl);

            response200(out, body, contentType);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200(OutputStream out, byte[] body, String contentType) {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
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

    private RequestLine parseRequestLine(BufferedReader br) throws IOException {
        String[] firstLine = br.readLine().split(" ");
        HttpMethod httpMethod = HttpMethod.findMethod(firstLine[0]).orElseThrow();
        String[] result = firstLine[1].split("\\?");
        RequestLine requestLine = new RequestLine(httpMethod, result[0]);
        if(result.length > 1) {
            requestLine.setQueryString(result[1]);
        }
        return requestLine;
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
