package model.http.response.httpresponse;

import model.http.ContentType;
import model.http.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static util.SpecialCharacters.NEW_LINE;

public class HttpResponse implements HttpServletResponse {

    private Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public void response(OutputStream out) throws IOException {
//        DataOutputStream dos = new DataOutputStream(out);
//
//        String requestURL = httpRequest.getRequestUrl();
//        String path = getPath(WEBAPP_ROOT, requestURL);
//
//        String[] extentionArray = requestURL.split(DOT);
//
//        String extention = extentionArray[extentionArray.length - 1];
//        byte[] body = Files.readAllBytes(new File(path).toPath());
//
//        if (httpRequest.isPost()) {
//            responseHeaderRedirection(dos, body.length, extention, requestURL);
//            responseBody(dos, body);
//            return;
//        }
//
//        responseHeader(dos, body.length, extention);
//        responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 " + "200" + " OK" + NEW_LINE);
            dos.writeBytes("Content-Type:" + ContentType.of(type) + NEW_LINE);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + NEW_LINE);
            dos.writeBytes(NEW_LINE);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeaderRedirection(DataOutputStream dos, int lengthOfBodyContent, String type, String location) {
        try {
            dos.writeBytes("HTTP/1.1 " + "302" + " OK" + NEW_LINE);
            dos.writeBytes("Content-Type:" + ContentType.of(type) + NEW_LINE);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + NEW_LINE);
            dos.writeBytes("Location: " + location);
            dos.writeBytes(NEW_LINE);
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

    @Override
    public DataOutputStream getDataOutputStream() {
        return null;
    }
}
