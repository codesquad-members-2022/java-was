package model.http.response.httpresponse;

import model.http.HttpStatusCode;
import model.http.HttpVersion;

public class ResponseStatusLine {

    private static final String RESPONSE_STATUSLINE_DELIMETER = " ";
    private HttpVersion httpVersion;
    private HttpStatusCode httpStatusCode;

    public ResponseStatusLine() {
        this.httpVersion = HttpVersion.HTTP_1_1;
        this.httpStatusCode = HttpStatusCode.OK;
    }

    public void changeStatusCode(HttpStatusCode httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return new ResponseStatusLineBuilder()
                .append(httpVersion)
                .append(RESPONSE_STATUSLINE_DELIMETER)
                .append(httpStatusCode)
                .append(RESPONSE_STATUSLINE_DELIMETER)
                .toString();
    }

    public static void main(String[] args) throws Exception {
        ResponseStatusLine re = new ResponseStatusLine();
        System.out.println(re.toString());
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
