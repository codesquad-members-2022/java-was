package webserver;

public abstract class Controller {

    public void process(HttpRequest httpRequest, HttpResponse httpResponse) {
        if (httpRequest.hasMethodEqualTo("GET")) {
            processGet(httpRequest, httpResponse);
        }
        if (httpRequest.hasMethodEqualTo("POST")) {
            processPost(httpRequest, httpResponse);
        }
    }

    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {

    }

    protected void processPost(HttpRequest httpRequest, HttpResponse httpResponse) {

    }
}
