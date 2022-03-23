package http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class HttpResponse {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private String httpMessage = "";

    public HttpResponse(String path) throws IOException {
        byte[] body = createResponseBody(path);
        response200Header(body.length);
        httpMessage += new String(body);
//        responseBody(body);
    }

    public byte[] createResponseBody(String path) throws IOException {
        if (!path.equals("/")) {
            return Files.readAllBytes(new File("./webapp/" + path).toPath());
        }
        return "Hello World".getBytes(StandardCharsets.UTF_8);
    }

    public void response200Header(int lengthOfBodyContent) {
        httpMessage += ("HTTP/1.1 200 OK \r\n");
        httpMessage += ("Content-Type: text/html;charset=utf-8\r\n");
        httpMessage += ("Content-Length: " + lengthOfBodyContent + "\r\n");
        httpMessage += ("\r\n");
    }

//    public void responseBody(DataOutputStream dos, byte[] body) {
//        try {
//            dos.write(body, 0, body.length);
//            dos.flush();
//        } catch (IOException e) {
////            log.error(e.getMessage());
//        }
//    }

    public String getHttpMessage() {
        return httpMessage;
    }
}
