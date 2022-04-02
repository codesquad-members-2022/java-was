package model.http.request.httprequest;

import model.http.request.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static model.http.HttpMethod.GET;
import static model.http.HttpMethod.POST;
import static util.SpecialCharacters.BLANK;

public class HttpRequest implements HttpServletRequest {

    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private RequestLine requestLine;
    private HttpHeader httpHeader;
    private HttpRequestBody body;

    public HttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        this.requestLine = getRequestLine(bufferedReader);
        this.httpHeader = getHttpHeader(bufferedReader);
        this.body = getHttpRequestBody(bufferedReader, getContentLength());
    }

    private RequestLine getRequestLine(BufferedReader bufferedReader) throws IOException {
        String[] requestLineArray = bufferedReader.readLine().split(BLANK);
        return new RequestLine(requestLineArray);
    }

    private HttpHeader getHttpHeader(BufferedReader bufferedReader) throws IOException {
        return new HttpHeader(bufferedReader);
    }

    private HttpRequestBody getHttpRequestBody(BufferedReader bufferedReader, int contentLength) throws IOException {
        return new HttpRequestBody(bufferedReader, contentLength);
    }

    private int getContentLength() {
        return httpHeader.getContentLength();
    }

    public String getBody() {
        return body.getBody();
    }

    @Override
    public boolean isGet() {
        return requestLine.getHttpMethod().equals(GET);
    }

    @Override
    public boolean isPost() {
        return requestLine.getHttpMethod().equals(POST);
    }

    @Override
    public String getRequestURL() {
        return null;
    }

}
