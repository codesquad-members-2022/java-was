package com.riakoader.was.httpmessage;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Headers {

    private final Map<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public Headers(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getValue(String name) {
        return headers.get(name);
    }

    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    public String getHeaderMessage() {
        return headers.keySet()
                .stream()
                .map(name -> name + ": " + getValue(name))
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
