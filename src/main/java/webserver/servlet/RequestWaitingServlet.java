package webserver.servlet;

import static webserver.servlet.ServletStatus.WAITING;

public class RequestWaitingServlet {

    private final int id;
    private ServletStatus servletStatus;

    public RequestWaitingServlet(int id) {
        this.id = id;
        this.servletStatus = WAITING;
    }

    public void changeServletStatus(ServletStatus servletStatus) {
        this.servletStatus = servletStatus;
    }

    public boolean isAvailable() {
        return servletStatus.isAvailable();
    }

    public ServletStatus getServletStatus() {
        return servletStatus;
    }
}
