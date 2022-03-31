package webserver;

import java.util.Map;

public class Request {

    private RequestLine requestLine;
    private Map<String, String> headers;
    private String messageBody;

    public Request(RequestLine requestLine, Map<String, String> headers, String messageBody) {
        this.requestLine = requestLine;
        this.headers = headers;
        this.messageBody = messageBody;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public boolean isGetMethodType() {
        return requestLine.isGetMethodType();
    }

    public boolean isPostMethodType() {
        return requestLine.isPostMethodType();
    }

    public URL getUrl() {
        return requestLine.getUrl();
    }

}

