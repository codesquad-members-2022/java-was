package db;

import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SessionDataBaseTest {

    private SessionDataBase sessionDataBase;

    @BeforeEach
    void beforeEach() {
        sessionDataBase = new SessionDataBase();
    }

    @Test
    void createSession() {
        User user = new User("qqq", "1234", "tany", "juni8453@naver.com");

        String sessionId = sessionDataBase.saveSession(user);
        System.out.println("sessionId = " + sessionId);
    }
}
