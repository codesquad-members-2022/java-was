package webserver.controller;

import webserver.MyHttpRequest;

public class HomeController implements MyController{

    @Override
    public String process(MyHttpRequest request) {
        return "index";
    }
}
