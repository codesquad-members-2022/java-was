package webserver;

import db.DataBase;
import model.http.Extention;
import model.request.HttpRequest;
import model.response.HttpResponse;
import model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static util.Pathes.JOIN_PATH;
import static util.SpecialCharacters.URL_BOUNDARY;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final int URL_INDEX = 1;

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = new HttpRequest(in);
            log.info("HttpRequest: {}", httpRequest);
            HttpResponse httpResponse = new HttpResponse();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
