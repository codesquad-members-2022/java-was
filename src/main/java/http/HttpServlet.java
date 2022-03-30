package http;

public abstract class HttpServlet {

    public Response service(Request request, Response response) {
        switch (request.getHttpMethod()) {
            case GET:
                return doGet(request, response);
            case POST:
                return doPost(request, response);
        }

        throw new IllegalStateException("Unsupported http method");
    }

    public Response doGet(Request request, Response response) {
        // TODO : GET 요청 처리는 이 메소드를 오버라이드 해서 구현
        throw new IllegalStateException("Unimplemented get method");
    }

    public Response doPost(Request request, Response response) {
        // TODO : POST 요청 처리는 이 메소드를 오버라이드 해서 구현
        throw new IllegalStateException("Unimplemented get method");
    }
}
