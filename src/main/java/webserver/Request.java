package webserver;

import java.util.Map;

public class Request {

    private String methodType;
    private String requestLine;
    private String messageBody;
    private URL url;

    public Request(String requestLine, String messageBody, String methodType, URL url) {
        this.requestLine = requestLine;
        this.messageBody = messageBody;
        this.methodType = methodType;
        this.url = url;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public String getRequestLine() {
        return requestLine;
    }

    public URL getURL() {
        return url;
    }

    public String getMethodType() {
        return methodType;
    }
}

