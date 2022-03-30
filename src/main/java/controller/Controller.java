package controller;

import webserver.Request;
import webserver.Response;

public interface Controller {

	void process(Request request, Response response);

}
