package controller;

import java.io.IOException;
import webserver.Request;
import webserver.Response;

public interface Controller {

	void process(Request request, Response response) throws IOException;

}
