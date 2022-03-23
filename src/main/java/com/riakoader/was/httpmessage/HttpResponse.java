package com.riakoader.was.httpmessage;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpResponse {

    private String statusLine;
    private final Map<String, String> headers = new HashMap<>();
    private byte[] body;


    public void setStatusLine(String protocol, HttpStatus status) {
        statusLine = protocol + " " + status;
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public byte[] toByteArray() {
        String responseMessage = statusLine + System.lineSeparator() +
                headers.keySet().stream()
                .map(name -> name + ": " + headers.get(name))
                .collect(Collectors.joining(System.lineSeparator()));

        responseMessage += System.lineSeparator();

        byte[] headerBytes = responseMessage.getBytes(StandardCharsets.UTF_8);
        byte[] responseMessageBytes = Arrays.copyOf(body, body.length);
        System.arraycopy(headerBytes, 0, responseMessageBytes, 0, headerBytes.length);
        System.out.println("responseMessageTest = " + new String(headerBytes));
        System.out.println("body = " + new String(body));
        System.out.println("responseMessageBytes = " + (body == responseMessageBytes));

        return responseMessageBytes;
    }
}
