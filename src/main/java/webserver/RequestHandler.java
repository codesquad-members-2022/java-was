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
import static util.SpecialCharacters.QUESTION_MARK;

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

            String requestUrl = httpRequest.requestUrl();

            if (requestUrl.startsWith(JOIN_PATH) && requestUrl.endsWith(Extention.HTML.getType())) {
                int index = requestUrl.indexOf(QUESTION_MARK);

                String queryString = requestUrl.substring(index + 1);
                Map<String, String> joinRequestParams = HttpRequestUtils.parseQueryString(queryString);
                User user = new User(joinRequestParams.get("userId"), joinRequestParams.get("password"), URLDecoder.decode(joinRequestParams.get("name"), StandardCharsets.UTF_8), joinRequestParams.get("email"));

                DataBase.addUser(user);
                log.debug("User : {}", user);

            }
            httpResponse.response(out, httpRequest.getRequestLine());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
