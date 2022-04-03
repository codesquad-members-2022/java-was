package webserver;

public class SessionValidator {
    private static final SessionValidator instance = new SessionValidator();

    private SessionValidator() {}

    public static SessionValidator getInstance() {
        if (instance == null) {
            return new SessionValidator();
        }
        return instance;
    }
}
