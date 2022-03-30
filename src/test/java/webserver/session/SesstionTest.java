package webserver.session;

import db.SessionDataBase;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SesstionTest {

    @Test
    void sessionDB_addSession() {
        String cookie = SessionDataBase.addSession("kukim");

        Optional<String> userid = SessionDataBase.findUserIdByCookie(cookie);

        assertThat(userid.get()).isEqualTo("kukim");
    }

    @Test
    void sessionDB_deleteByCookie() {
        String cookie = SessionDataBase.addSession("kukim");
        String cookie2 = SessionDataBase.addSession("terry");
        SessionDataBase.deleteByCookie(cookie2);

        Optional<String> userid = SessionDataBase.findUserIdByCookie(cookie2);

        assertThat(userid).isEmpty();
    }
}
