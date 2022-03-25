package webserver.handler;

import webserver.Request;
import webserver.Response;

public interface PathHandler {

    Response handle(Request request);
}
