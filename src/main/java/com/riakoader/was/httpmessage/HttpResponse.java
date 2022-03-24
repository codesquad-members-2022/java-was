package com.riakoader.was.httpmessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private String statusLine;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body = "".getBytes();

    public void setStatusLine(String protocol, HttpStatus status) {
        statusLine = protocol + " " + status;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeaderMessage() {
        return headers.keySet().stream().map(name -> name + ": " + headers.get(name))
                .collect(Collectors.joining(System.lineSeparator()));
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        String headerMessage = statusLine + System.lineSeparator() +
                getHeaderMessage() + System.lineSeparator() + System.lineSeparator();

        byte[] headerBytes = headerMessage.getBytes(StandardCharsets.UTF_8);
        byte[] responseMessageBytes = Arrays.copyOf(body, body.length);

        byte[] httpResponse = new byte[headerBytes.length + responseMessageBytes.length];
        System.arraycopy(headerBytes, 0, httpResponse, 0, headerBytes.length);
        System.arraycopy(responseMessageBytes, 0, httpResponse, headerBytes.length, responseMessageBytes.length);

        return httpResponse;
    }
}
