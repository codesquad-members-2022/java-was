package http;

public abstract class HttpServlet {

    protected Request request;
    protected Response response;

    protected HttpServlet(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    public Response service() {
        switch (request.getHttpMethod()) {
            case GET:
                return doGet();
            case POST:
                return doPost();
        }

        throw new IllegalStateException("Unsupported http method");
    }

    public Response doGet() {
        // TODO : GET 요청 처리는 이 메소드를 오버라이드 해서 구현
        throw new IllegalStateException("Unimplemented get method");
    }

    public Response doPost() {
        // TODO : POST 요청 처리는 이 메소드를 오버라이드 해서 구현
        throw new IllegalStateException("Unimplemented get method");
    }
}
