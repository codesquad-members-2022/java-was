package webserver.controller;

import webserver.Request;
import webserver.Response;

import java.io.IOException;

public abstract class Controller {

	protected Controller() {
	}

	public void process(Request request, Response response) throws IOException {
	}
}
