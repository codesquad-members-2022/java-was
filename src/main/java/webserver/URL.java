package webserver;

public class URL {

    private static final String BASE_PATH = "http://localhost:8080";
    private static final String HOME_PAGE_PATH = "/index.html";
    private static final String SINE_UP_PAGE_PATH = "/user/form.html";
    private static final String LOGIN_FAILED_PAGE_PATH = "/user/login_failed.html";

    private String path;
    private String redirectPath;

    public URL(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getRedirectPath() {
        return redirectPath;
    }

    public void changePathToHomePage() {
        redirectPath = BASE_PATH + HOME_PAGE_PATH;
    }

    public void changePathToSignUpPage() {
        redirectPath = BASE_PATH + SINE_UP_PAGE_PATH;
    }

    public void changePathToLoginFailedPage() {
        redirectPath = BASE_PATH + LOGIN_FAILED_PAGE_PATH;
    }

    public boolean comparePath(String targetPath) {
        return path.equals(targetPath);
    }
}
