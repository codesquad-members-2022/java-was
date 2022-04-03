package model.http.response.httpresponse;

import model.http.HttpStatusCode;
import model.http.HttpVersion;

public class ResponseStatusLine {

    private static final String RESPONSE_STATUSLINE_DELIMETER = " ";
    public static final String NEW_LINE = "\r\n";

    private HttpVersion httpVersion;
    private HttpStatusCode httpStatusCode;

    public ResponseStatusLine() {
        this.httpVersion = HttpVersion.HTTP_1_1;
        this.httpStatusCode = HttpStatusCode.OK;
    }

    public void changeStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String get200StatusStatusLine(HttpVersion httpVersion, HttpStatusCode statusCode) {
        return new ResponseStatusLineBuilder()
                .append(httpVersion)
                .append(RESPONSE_STATUSLINE_DELIMETER)
                .append(statusCode)
                .append(NEW_LINE)
                .toString();
    }

    private static class ResponseStatusLineBuilder {
        private StringBuffer stringBuffer = new StringBuffer();

        private ResponseStatusLineBuilder() {
        }

        public ResponseStatusLineBuilder append(Object value) {
            stringBuffer.append(value);
            return this;
        }

        @Override
        public String toString() {
            return stringBuffer.toString();
        }
    }
}
