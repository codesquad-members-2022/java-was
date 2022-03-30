package webserver.controller;

import webserver.MyHttpRequest;

public interface MyController {
    public String process(MyHttpRequest request);
}
