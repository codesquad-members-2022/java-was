package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
import util.IOUtils;
import webserver.controller.ControllerFactory;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String[] tokens = readRequestLine(br);
            String method = tokens[0];
            String url = tokens[1];
            String version = tokens[2];
            Map<String, String> requestHeader = readRequestHeader(br);
            String requestBody = null;

            if (method.equals("POST")) {
                int contentLength = Integer.parseInt(requestHeader.get("Content-Length"));
                requestBody = IOUtils.readData(br, contentLength);

                log.debug("Body: {}", requestBody);
                log.debug("Content-Length: {}", requestBody.length());
            }

            HttpRequest request = new HttpRequest(method, url, version, requestHeader, requestBody);

            HttpResponse response = ControllerFactory.getResponse(request);

            byte[] responseBody = response.getResponseBody();
            writeHeaders(dos, responseBody.length, response);
            responseBody(dos, responseBody);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String[] readRequestLine(BufferedReader br) throws IOException {
        String l = br.readLine();
        log.debug("Request Line: {}", l);
        String line = URLDecoder.decode(l, StandardCharsets.UTF_8);
        return line.split(" ");
    }

    private Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
        String line;
        Map<String, String> requestHeaderMap = new HashMap<>();
        while (!(line = br.readLine()).isBlank()) {
            Pair pair = HttpRequestUtils.parseHeader(line);
            requestHeaderMap.put(pair.getKey(), pair.getValue());
        }
        return requestHeaderMap;
    }

    private void writeHeaders(DataOutputStream dos, int lengthOfBodyContent, HttpResponse response) {
        try {
            dos.writeBytes(String.format("%s %d %s %s",
                    response.getVersion(), response.getHttpStatusCode(), response.getHttpStatusMessage(), System.lineSeparator()));
            Map<String, String> responseHeaders = response.getResponseHeaders();

            for (Map.Entry<String, String> entry : responseHeaders.entrySet()) {
                dos.writeBytes(String.format("%s: %s %s", entry.getKey(), entry.getValue(), System.lineSeparator()));
            }

            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
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
