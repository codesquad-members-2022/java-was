package webserver;

public class StaticResourceController extends Controller  {

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        httpResponse.responseStaticResource(RESOURCE_ROOT + httpRequest.getPath());
    }
}
