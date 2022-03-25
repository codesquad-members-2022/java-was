package webserver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Request;
import webserver.Response;
import webserver.Status;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DefaultFileHandler implements PathHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultFileHandler.class);
    private static final String WEBAPP_PATH = "./webapp";

    @Override
    public Response handle(Request request) {
        try {
            byte[] body = readFile(request);
            return new Response.Builder(Status.OK)
                    .addHeader("Content-Type", request.getContentType().getMime())
                    .addHeader("Content-Length", String.valueOf(body.length))
                    .body(body)
                    .build();

        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private byte[] readFile(Request request) throws IOException {
        return Files.readAllBytes(new File(WEBAPP_PATH + request.parsePath()).toPath());
    }
}
