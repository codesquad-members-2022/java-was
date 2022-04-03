package webserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StaticResourceController extends Controller  {

    private static final String FILE_EXTENSION_SEPARATOR = ".";

    @Override
    protected void processGet(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            String path = RESOURCE_ROOT + httpRequest.getPath();
            byte[] body = Files.readAllBytes(Path.of(path));

            httpResponse.addHeader("Content-Type", getContentType(path));
            httpResponse.addHeader("Content-Length", "" + body.length);
            httpResponse.sendHeader();
            httpResponse.sendBody(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getContentType(String path) {
        String extension = getFileExtension(path);
        return ContentTypeMapping.getContentType(extension);
    }

    private String getFileExtension(String path) {
        int index = path.lastIndexOf(FILE_EXTENSION_SEPARATOR);

        return index == -1 ? "" : path.substring(index);
    }
}
