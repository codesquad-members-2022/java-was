package com.riakoader.was.httpmessage;

import com.google.common.base.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private final String protocol;
    private HttpStatus status;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body = "".getBytes();

    public HttpResponse(String protocol) {
        this.protocol = protocol;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getHeaderMessage() {
        return headers.keySet()
                .stream()
                .map(name -> name + ": " + headers.get(name))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        String httpRequestMessage = protocol + " " + status + System.lineSeparator() +
                getHeaderMessage() + Strings.repeat(System.lineSeparator(), 2) +
                new String(Arrays.copyOf(body, body.length));

        return httpRequestMessage.getBytes(StandardCharsets.UTF_8);
    }
}
