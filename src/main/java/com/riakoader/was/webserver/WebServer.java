package com.riakoader.was.webserver;

import java.net.ServerSocket;
import java.net.Socket;

import com.riakoader.was.config.WebServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebServer {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class);

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        WebServerConfig config = WebServerConfig.getInstance();
        FrontHandler frontHandler = FrontHandler.getInstance();

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                frontHandler.assign(connection);
            }
        }
    }
}
