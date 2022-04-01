package com.riakoader.was.httpmessage;

import com.google.common.base.Strings;
import com.riakoader.was.session.Cookie;
import com.riakoader.was.session.Cookies;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpResponse {

    private final String protocol;
    private HttpStatus status;
    private final Headers headers = new Headers();
    private final Cookies cookies = new Cookies();
    private byte[] body = "".getBytes();

    public HttpResponse(String protocol) {
        this.protocol = protocol;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setHeader(String name, String value) {
        headers.setHeader(name, value);
    }

    public void addCookie(Cookie cookie) {
        cookies.add(cookie);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        String httpRequestMessage = protocol + " " + status + System.lineSeparator() +
                headers.getHeaderMessage() + System.lineSeparator() +
                cookies.toSetCookieMessage() + Strings.repeat(System.lineSeparator(), 2) +
                new String(Arrays.copyOf(body, body.length));

        return httpRequestMessage.getBytes(StandardCharsets.UTF_8);
    }
}
