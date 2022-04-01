package model.response.httpresponse;

import model.http.Extention;
import model.request.httprequest.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import static util.HttpRequestUtils.getPath;
import static util.Pathes.WEBAPP_ROOT;
import static util.SpecialCharacters.DOT;
import static util.SpecialCharacters.NEW_LINE;

public class HttpResponse {

    private Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public void response(OutputStream out, HttpRequest httpRequest) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String requestUrl = httpRequest.getRequestUrl();
        String path = getPath(WEBAPP_ROOT, requestUrl);

        String[] extentionArray = requestUrl.split(DOT);

        String extention = extentionArray[extentionArray.length - 1];
        byte[] body = Files.readAllBytes(new File(path).toPath());

        if (httpRequest.isPost()) {
            responseHeaderRedirection(dos, body.length, extention, requestUrl);
            responseBody(dos, body);
            return;
        }

        responseHeader(dos, body.length, extention);
        responseBody(dos, body);
    }

    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 " + "200" + " OK" + NEW_LINE);
            dos.writeBytes("Content-Type:" + Extention.of(type) + NEW_LINE);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + NEW_LINE);
            dos.writeBytes(NEW_LINE);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseHeaderRedirection(DataOutputStream dos, int lengthOfBodyContent, String type, String location) {
        try {
            dos.writeBytes("HTTP/1.1 " + "302" + " OK" + NEW_LINE);
            dos.writeBytes("Content-Type:" + Extention.of(type) + NEW_LINE);
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
}
