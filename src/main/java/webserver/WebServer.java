package webserver;

import configuration.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.servlet.ConnectionPool;

import java.net.ServerSocket;
import java.net.Socket;

import static configuration.ObjectFactory.dispatcherServlet;

public class WebServer {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class);
    private static final int DEFAULT_PORT = 8082;
    private static final ConnectionPool connectionPool = ObjectFactory.connectionPool;

    public static void main(String args[]) throws Exception {
        int port = 0;
        if (args == null || args.length == 0) {
            port = DEFAULT_PORT;
        } else {
            port = Integer.parseInt(args[0]);
        }

        try (ServerSocket listenSocket = new ServerSocket(port)) {
            log.info("Web Application Server started {} port.", port);

            Socket connection;
            while ((connection = listenSocket.accept()) != null) {
                if (connectionPool.hasAvailableServlet()) {
                    RequestHandler requestHandler = new RequestHandler(connection, dispatcherServlet);
                    requestHandler.start();
                }
            }
        }
    }
}
