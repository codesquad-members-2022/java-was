package webserver;

import db.DataBase;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.RequestLineUtil;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            Request request = makeRequest(br);

            // URL init
            URL url = request.getURL();

            // user save
            if (request.getMethodType().equals("POST") && url.comparePath("/user/create")) {
                String messageBody = request.getMessageBody();
                userSave(messageBody, url);
            }

            // Response Message
            DataOutputStream dos = new DataOutputStream(out);
            Response response = new Response(request.getRequestLine(), url);
            sendResponse(dos, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void userSave(String messageBody, URL url) {
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(messageBody);
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"),
            userInfo.get("email"));

        if (DataBase.findUserById(userInfo.get("userId")) == null) {
            DataBase.addUser(user);
            url.setRedirectHomePage();
        } else {
            url.setRedirectSignUpPage();
        }
    }

    private void sendResponse(DataOutputStream dos, Response response) throws IOException {
        response.action();

        String header = response.getHeaders();
        dos.writeBytes(header);

        byte[] body = response.getBody();
        if (body != null) {
            dos.write(body, 0, body.length);
        }
    }


    private Request makeRequest(BufferedReader bufferedReader) throws IOException {
        String requestLine = initRequestLine(bufferedReader);
        Map<String, String> headers = new HashMap<>();

        initRequestHeaders(bufferedReader, headers);
        outputLog(headers, requestLine);

        URL url = initURL(requestLine, headers);

        String messageBody = "";
        String methodType = RequestLineUtil.getMethodType(requestLine);

        if (methodType.equals("POST")) {
            messageBody = IOUtils.readData(bufferedReader,
                Integer.parseInt(headers.get("Content-Length:")));
            log.debug(messageBody);
        }

        return new Request(requestLine, messageBody, methodType, url);
    }

    private void outputLog(Map<String, String> headers, String requestLine) {
        // Request Line Log
        log.debug(requestLine);
        // Request Headers Log
        for (Entry<String, String> entry : headers.entrySet()) {
            log.debug("Request: {} {}", entry.getKey(), entry.getValue());
        }
    }

    private String initRequestLine(BufferedReader bufferedReader) throws IOException {
        String line;
        if ("".equals(line = bufferedReader.readLine()) || line == null) {
            throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
        }
        return line;
    }

    private void initRequestHeaders(BufferedReader bufferedReader, Map<String, String> headers)
        throws IOException {
        String line;
        while (!"".equals(line = bufferedReader.readLine())) {
            if (line == null) {
                throw new IllegalArgumentException("Request.bufferedReader.readLine == null 입니다.");
            }
            String[] splitLine = line.split(" ");
            headers.put(splitLine[0], splitLine[1]);
        }
    }

    private URL initURL(String requestLine, Map<String, String> headers) {
        String decodedUrl = URLDecoder.decode(requestLine, StandardCharsets.UTF_8);
        String host = headers.get("Host:");
        return new URL(RequestLineUtil.getURL(decodedUrl), host);
    }
}
