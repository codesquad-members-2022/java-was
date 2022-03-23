package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.HttpRequest;
import http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.MyController;
import controller.SaveUserController;
import util.HttpRequestUtils;
import util.IOUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private final Map<String, MyController> handlerMapper = new HashMap<>();

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initHandlerMapper();
    }

    private void initHandlerMapper() {
        handlerMapper.put("/user/create", new SaveUserController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream dos = new DataOutputStream(out);

            HttpRequest httpRequest = new HttpRequest(bf);

            String path = httpRequest.getPath();

            if (handlerMapper.containsKey(path)) {
                MyController myController = handlerMapper.get(path);

                Map<String, String> paramMap = httpRequest.getParamMap();
                path = myController.process(paramMap);
            }

            IOUtils.printRequestHeader(httpRequest.getHeaderMessages());

            HttpResponse httpResponse = new HttpResponse(path);
            dos.writeBytes(httpResponse.getHttpMessage());
            dos.flush();
            System.out.println(httpResponse.getHttpMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
