package controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;

public interface Controller {

    void service(HttpRequest request, HttpResponse response) throws IOException;
}
