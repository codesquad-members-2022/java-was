package model.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static util.SpecialCharacters.BLANK;

public class HttpRequest {

    private RequestLine requestLine;
    private HttpHeader httpHeader;
    private HttpRequestBody httpRequestBody;
    private static final StringBuffer sb = new StringBuffer();

    public HttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        this.requestLine = getRequestLine(bufferedReader);
        this.httpHeader = getHttpHeader(bufferedReader);
    }

    private RequestLine getRequestLine(BufferedReader bufferedReader) throws IOException {
        String[] requestLineArray = bufferedReader.readLine().split(BLANK);
        return new RequestLine(requestLineArray);
    }

    private HttpHeader getHttpHeader(BufferedReader bufferedReader) throws IOException {
        return new HttpHeader(bufferedReader);
    }

    public String requestUrl(){
        return requestLine.getHttpRequestUrl();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }
}
