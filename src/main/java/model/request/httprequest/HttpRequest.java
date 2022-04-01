package model.request.httprequest;

import db.DataBase;
import model.http.HttpMethod;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static model.http.HttpMethod.POST;
import static util.SpecialCharacters.BLANK;

public class HttpRequest {

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

    public String getRequestUrl() {
        String requestUrl = requestUrl();
        if (getHttpMethod().equals(POST)) {
            String body = getBody();
            Map<String, String> joinRequestParams = HttpRequestUtils.parseQueryString(body);
            User user = new User(joinRequestParams.get("userId"), joinRequestParams.get("password"), URLDecoder.decode(joinRequestParams.get("name"), StandardCharsets.UTF_8), joinRequestParams.get("email"));
            requestUrl = "/index.html";
            DataBase.validateDuplicateId(user.getUserId());
            DataBase.addUser(user);
            log.debug("User : {}", user);
        }
        return requestUrl;
    }

    public HttpMethod getHttpMethod() {
        return requestLine.getHttpMethod();
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

    public String requestUrl(){
        return requestLine.getHttpRequestUrl();
    }

    public RequestLine getRequestLine() {
        return requestLine;
    }

    public String getBody() {
        return body.getBody();
    }

    public boolean isPost() {
        return requestLine.getHttpMethod().equals(POST);
    }
}
