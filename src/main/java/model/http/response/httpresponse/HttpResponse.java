package model.http.response.httpresponse;

import model.http.ContentType;
import model.http.response.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static model.http.HttpStatusCode.OK;
import static model.http.HttpVersion.HTTP_1_1;
import static util.SpecialCharacters.NEW_LINE;

public class HttpResponse implements HttpServletResponse {

    private ResponseStatusLine responseStatusLine;
    private DataOutputStream dos;

    public HttpResponse(OutputStream outputStream) {
        this.dos = new DataOutputStream(outputStream);
        this.responseStatusLine = new ResponseStatusLine();
    }

    private Logger log = LoggerFactory.getLogger(HttpResponse.class);

    public void responseHeader(int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes(get200ResponseStatusLine());
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

    public void responseBody(byte[] body) {
        try {
            this.dos.write(body, 0, body.length);
            this.dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public String get200ResponseStatusLine() {
        return responseStatusLine.get200StatusStatusLine(HTTP_1_1, OK);
    }

    @Override
    public DataOutputStream getDos() {
        return dos;
    }
}
