package db;

import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import model.User;

class DataBaseTest {
    @Test
    @DisplayName("중복된 아이디로 가입하면 DB에 저장되지 않고, Optional.empty()를 반환한다")
    void addUser() {
        Map<String, User> users = new HashMap<>();
        User user1 = new User("BC", "123", "123", "3423@adw.com");
        User user2 = new User("BC", "1234", "bcbcw", "3423@333adw.com");

        DataBase.addUser(user1);
        Optional<User> testUser2 = DataBase.addUser(user2);
        Collection<User> all = DataBase.findAll();

        assertThat(testUser2).isEmpty();
        assertThat(all.size()).isEqualTo(1);
    }
}
