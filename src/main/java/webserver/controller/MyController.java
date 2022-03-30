package webserver.controller;

import webserver.MyHttpRequest;
import webserver.MyHttpResponse;

public interface MyController {
    public String process(MyHttpRequest request, MyHttpResponse response);
}
