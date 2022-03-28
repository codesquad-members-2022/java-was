package webserver;

public class URL {

    private static final String SCHEME = "http://";
    private static final String HOME_PAGE_PATH = "/index.html";
    private static final String SINE_UP_PAGE_PATH = "/user/form.html";

    private final String homePage;
    private final String signUpPage;

    private String path;

    public URL(String path, String host) {
        this.path = path;
        homePage = SCHEME + host + HOME_PAGE_PATH;
        signUpPage = SCHEME + host + SINE_UP_PAGE_PATH;
    }

    public String getPath() {
        return path;
    }

    public void setRedirectHomePage() {
        path = homePage;
    }

    public void setRedirectSignUpPage() {
        path = signUpPage;
    }

    public boolean comparePath(String targetPath) {
        return path.equals(targetPath);
    }
}
