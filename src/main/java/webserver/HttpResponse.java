package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Pair;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpResponse {

    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
    private static final String HTTP_VERSION = "HTTP/1.1";
    public static final String STATUS_CODE_200_OK = "200 OK";
    public static final String STATUS_CODE_302_Found = "302 Found";
    private static final String CRLF = "\r\n";

    private final DataOutputStream dos;
    private String statusCode = STATUS_CODE_200_OK;
    private final List<Pair> headers = new ArrayList<>();

    HttpResponse(DataOutputStream dos) {
        this.dos = dos;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void addHeader(String name, String value) {
        headers.add(new Pair(name, value));
    }

    public void sendHeader() {
        try {
            dos.writeBytes(HTTP_VERSION + " " + statusCode + CRLF);

            for (Pair pair : headers) {
                dos.writeBytes(pair.getKey() + ": " + pair.getValue() + CRLF);
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void sendBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void redirectTo(String redirectPath) {
        setStatusCode(STATUS_CODE_302_Found);
        addHeader("Location", redirectPath);
        addHeader("Content-Type", "text/html;charset=utf-8");
        sendHeader();
    }

}
