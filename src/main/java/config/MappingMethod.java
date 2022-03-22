package config;

public class MappingMethod {
    private Class controller;
    private String method;

    public Class getController() {
        return controller;
    }

    public void setController(Class controller) {
        this.controller = controller;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
