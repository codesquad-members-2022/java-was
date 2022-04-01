package com.riakoader.was.httpmessage;

import java.util.Map;

public class Parameters {

    private final Map<String, String> params;

    public Parameters(Map<String, String> params) {
        this.params = params;
    }

    public String getValue(String name) {
        return params.get(name);
    }
}
