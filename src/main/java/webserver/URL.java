package webserver;

public class URL {

    private static final String HOME_PAGE = "http://localhost:8080/index.html";
    private static final String SIGN_UP_PAGE = "http://localhost:8080/user/form.html";

    private String path;

    public URL(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setRedirectHomePage() {
        path = HOME_PAGE;
    }

    public void setRedirectSignUpPAGE() {
        path = SIGN_UP_PAGE;
    }

    public boolean comparePath(String target) {
        return path.equals(target);
    }
}
