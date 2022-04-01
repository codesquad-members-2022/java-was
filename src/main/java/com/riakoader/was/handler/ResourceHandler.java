package com.riakoader.was.handler;

import com.riakoader.was.httpmessage.HttpRequest;
import com.riakoader.was.httpmessage.HttpResponse;
import com.riakoader.was.httpmessage.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class ResourceHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResourceHandler.class);

    private static volatile ResourceHandler handler;

    private ResourceHandler() {
    }

    public static ResourceHandler getInstance() {
        if (handler == null) {
            synchronized (ResourceHandler.class) {
                if (handler == null) {
                    handler = new ResourceHandler();
                }
            }
        }
        return handler;
    }

    public HttpResponse service(HttpRequest request) throws IOException {
        HttpResponse response = new HttpResponse(request.getProtocol());

        Properties properties = new Properties();
        properties.load(new FileReader("src/main/resources/env.properties"));

        byte[] body;
        try {
            body = Files.readAllBytes(
                    new File(properties.getProperty("webapp_path") + request.getRequestURI()).toPath()
            );
        } catch (IOException e) {
            logger.error(e.getMessage());
            response.setStatus(HttpStatus.NOT_FOUND);
            return response;
        }

        response.setHeader("Content-Length", Integer.toString(body.length));
        response.setBody(body);
        response.setStatus(HttpStatus.OK);

        return response;
    }
}
