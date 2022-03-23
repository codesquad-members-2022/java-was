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
        throw new IllegalStateException("Unimplemented get method");
    }

    public Response doPost() {
        throw new IllegalStateException("Unimplemented post method");
    }
}
