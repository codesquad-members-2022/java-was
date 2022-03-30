package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);
            Request request = createRequest(bufferedReader);
            Response response = createResponse(request);
            String httpStateCode = sendResponse(request, response, dos);
            log.debug("HTTP stateCode={}", httpStateCode);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String sendResponse(Request request, Response response, DataOutputStream dos) {
        if (request.getMethod().equals("POST") && request.getUrl().equals("/user/create")) {
            User user = createUser(request);
            log.debug("user={}", user);
            DataBase.addUser(user);
            response.header(dos);
            response.body(dos);
            return "302";
        }
        response.header(dos);
        response.body(dos);
        return "200";
    }

    private Response createResponse(Request request) {
        return new Response(request.getMethod(), request.getUrl());
    }

    private Request createRequest(BufferedReader bufferedReader) {
        String headerLine = "";
        Map<String, String> headers = new HashMap<>();
        try {
            headerLine = bufferedReader.readLine();
            headers = readHeader(bufferedReader);
            if (headerLine.contains("POST")) {
                Request request = new Request(headerLine, headers);
                request.generateBody(bufferedReader);
                return request;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return new Request(headerLine, headers);
    }

    private Map<String, String> readHeader(BufferedReader bufferedReader) {
        Map<String, String> headers = new HashMap<>();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line.equals("")) break;
                HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                String key = pair.getKey();
                String value = pair.getValue();
                headers.put(key, value);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return headers;
    }

    private User createUser(Request request) {
        String requestBody = request.getBody();
        Map<String, String> userData = HttpRequestUtils.parseQueryString(requestBody);
        return new User(userData.get("userId"),
                userData.get("password"),
                userData.get("name"),
                userData.get("email"));
    }
}
