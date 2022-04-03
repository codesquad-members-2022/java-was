package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyHttpResponse {

    private static final Logger log = LoggerFactory.getLogger(MyHttpResponse.class);

    private final DataOutputStream dos;
    private final MyHttpRequest myHttpRequest;
    private String viewName;

    public MyHttpResponse(OutputStream out, MyHttpRequest myHttpRequest) {
        this.dos =  new DataOutputStream(out);
        this.myHttpRequest = myHttpRequest;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public void sendResponse() throws IOException {
        byte[] body = null;
        log.debug("viewName={}", viewName);
        if (viewName.startsWith("redirect:")) {
            String redirectURL = viewName.substring(viewName.indexOf(":") + 1);
            responseRedirect(redirectURL);
            return;
        }

        body = Files.readAllBytes(new File("./webapp" + "/" + viewName + ".html").toPath());
        responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.OK);
        responseBody(dos, body);
    }

    public void send404page() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/error/404.html").toPath());
        responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.NOT_FOUND);
        responseBody(dos, body);
    }

    public void sendStaticResponse() throws IOException {
        File file = new File("./webapp" + myHttpRequest.getRequestURI());
        if (!file.exists()) {
            log.info("cannot find static resource : {}", myHttpRequest.getRequestURI());
            send404page();
            return;
        }
        byte[] body = Files.readAllBytes(file.toPath());
        responseHeader(dos, body.length, myHttpRequest, HttpResponseStatusCode.OK);
        responseBody(dos, body);
    }

    private void responseRedirect(String redirectURL) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found\r\n");
        dos.writeBytes("Location:" + redirectURL + "\r\n");
    }
    private void responseHeader(DataOutputStream dos, int lengthOfBodyContent, MyHttpRequest request, HttpResponseStatusCode statusCode) {
        try {
            dos.writeBytes(statusCode.getValue());
            dos.writeBytes("Content-Type: " + request.getHeader("Accept")[0] + "\r\n");
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
