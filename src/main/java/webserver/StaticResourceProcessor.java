package webserver;

import java.io.IOException;

import util.IOUtils;

public class StaticResourceProcessor {

    private static final String staticResourcePath = System.getProperty("user.dir") + "/webapp";

    private static StaticResourceProcessor instance;

    private StaticResourceProcessor() { }

    public synchronized static StaticResourceProcessor getInstance() {
        if (instance == null) {
            instance = new StaticResourceProcessor();
        }

        return instance;
    }

    public byte[] readStaticResource(String requestUrl) throws IOException {
        return IOUtils.readFile(staticResourcePath + requestUrl);
    }
}
