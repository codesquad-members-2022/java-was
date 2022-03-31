package webserver;

public class StaticResourceController extends Controller  {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.responseStaticResource(httpRequest.getPath());
    }
}
