package webserver;

import model.Extention;
import model.HeaderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static util.HttpRequestUtils.getPath;
import static util.Pathes.WEBAPP_ROOT;
import static util.SpecialCharacters.*;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final int REQUEST_INDEX = 1;

    private Socket connection;
    private Configuration configuration = new Configuration();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            Map<HeaderType, String> headerValues = getHeaders(bufferedReader);
            Request request = new Request(headerValues, "", "", "", "");

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private Map<HeaderType, String> getHeaders(BufferedReader bufferedReader) throws IOException {
        Map<HeaderType, String> headers = new ConcurrentHashMap<>();

        String[] requestLine = bufferedReader.readLine().split(BLANK);
        addHeaderLineTypes(headers, requestLine);

        String line;
        while (!(line = bufferedReader.readLine()).equals(NULL_STRING)) {
            addHeader(headers, line);
        }
        return headers;
    }

    private void addHeader(Map<HeaderType, String> headers, String line) {
        String key = line.split(":")[0];
        String value = line.split(": ")[1];
        headers.put(configuration.getHeaderType().get(key), value);
    }

    private void addHeaderLineTypes(Map<HeaderType, String> headers, String[] requestLine) {
        String httpMethod = requestLine[0];
        String requestURL = requestLine[1];

        addHeaderLineTypes(headers, "Request Method", httpMethod);
        addHeaderLineTypes(headers, "Request URL", requestURL);
    }

    private void addHeaderLineTypes(Map<HeaderType, String> headers, String type, String value) {
        headers.put(HeaderType.of(type), value);
    }

    private String getRequestUrl(BufferedReader bufferedReader) throws IOException {
        String[] requestLine = bufferedReader.readLine().split(BLANK);
        return requestLine[REQUEST_INDEX];
    }

    private void printHeaders(BufferedReader bufferedReader) throws IOException {
        String line;
        while (!(line = bufferedReader.readLine()).equals(NULL_STRING)) {
            log.debug("header: {} ", line);
        }
    }

    private void makeResponseBody(OutputStream out, String requestUrl) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        String path = getPath(WEBAPP_ROOT, requestUrl);

        // 추후 따로 Parser 만드는 것 고려
        String[] extentionArray = requestUrl.split(DOT);
        String extention = extentionArray[extentionArray.length - 1];

        byte[] body = Files.readAllBytes(new File(path).toPath());

        response200Header(dos, body.length, extention);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String type) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK" + ENTER);
            dos.writeBytes("Content-Type:" + Extention.of(type) + ENTER);
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + ENTER);
            dos.writeBytes(ENTER);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
