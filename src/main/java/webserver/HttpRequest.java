package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import util.HttpRequestUtils;
import util.IOUtils;

public class HttpRequest {

    private BufferedReader br;
    private RequestLine requestLine;
    private Map<String, String> headers;
    private Map<String, String> parameters;

    public HttpRequest(BufferedReader br) throws IOException {
        this.br = br;
        init();
    }

    private void init() throws IOException {
        String requestLine = URLDecoder.decode(br.readLine(), StandardCharsets.UTF_8);
        String[] requestLineSplit = requestLine.split(" ");
        this.requestLine = new RequestLine(requestLineSplit[0], requestLineSplit[1], requestLineSplit[2]);
        this.headers = IOUtils.readRequestHeader(br);
        this.parameters = parseParameter();
    }

    private Map<String, String> parseParameter() throws IOException {
        if (requestLine.isPost()) {
            String queryString = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            return HttpRequestUtils.parseQueryString(queryString);
        }
        return requestLine.parseParameter();
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public boolean isPost() {
        return requestLine.isPost();
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

}
