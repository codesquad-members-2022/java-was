package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.mapper.Filter;
import webserver.mapper.PathMapper;

public class RequestHandler extends Thread {

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;
    private final PathMapper pathMapper;
    private final Filter filter;

    private RequestReader requestReader;
    private ResponseWriter responseWriter;

    public RequestHandler(Socket connectionSocket, PathMapper pathMapper, Filter filter) {
        this.connection = connectionSocket;
        this.pathMapper = pathMapper;
        this.filter = filter;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
            connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Request request = parseRequest(in);
            Response response = handlePath(request);
            sendResponse(out, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Request parseRequest(InputStream in) throws IOException {
        requestReader = new RequestReader(in);
        return requestReader.create();
    }

    private void sendResponse(OutputStream out, Response response) {
        responseWriter = new ResponseWriter(new DataOutputStream(out));
        responseWriter.from(response);
    }

    private Response handlePath(Request request) {
        return Optional.ofNullable(filter.handle(request))
            .orElseGet(() -> pathMapper.callHandler(request));
    }
}
