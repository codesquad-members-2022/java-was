package was.webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import was.http.HttpRequest;
import was.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import was.controller.MyController;
import was.controller.SaveUserController;
import was.util.IOUtils;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            DataOutputStream outputStream = new DataOutputStream(out);
            HttpRequest httpRequest = new HttpRequest(bufferedReader);

            String path = httpRequest.getPath();

            if (handlerMapper.containsKey(path)) {
                MyController myController = handlerMapper.get(path);

                Map<String, String> paramMap = httpRequest.getParamMap();
                path = myController.process(paramMap);
            }

            IOUtils.printRequestHeader(httpRequest.getHeaderMessages());

            HttpResponse httpResponse = new HttpResponse(path, outputStream);
            outputStream.writeBytes(httpResponse.getResponseHeader());
            outputStream.write(httpResponse.getResponseBody());
            outputStream.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
