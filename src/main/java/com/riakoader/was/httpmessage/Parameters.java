package com.riakoader.was.httpmessage;

import java.util.HashMap;
import java.util.Map;

public class Parameters {

    private final Map<String, String> params;

    public Parameters() {
        this.params = new HashMap<>();
    }

    public Parameters(Map<String, String> params) {
        this.params = params;
    }

    public String getValue(String name) {
        return params.get(name);
    }

    public void setParameter(String name, String value) {
        params.put(name, value);
    }
}
