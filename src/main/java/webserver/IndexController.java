package webserver;

public class IndexController extends Controller {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.response302Header("/index.html");
    }
}
