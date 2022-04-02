package db;

import configuration.ObjectFactory;
import model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DataBaseTest {

    private User user;
    private User duplicateIdUser;
    private DataBase dataBase;

    private User getUser() {
        return new User("Min", "1234", "송민제", "Min@gmail.com");
    }

    @BeforeEach
    void init() {
        user = getUser();
        duplicateIdUser = getUser();
        dataBase = ObjectFactory.dataBase;
        dataBase.clear();
    }

    @Test
    @DisplayName("동일한 아이디의 회원이 존재하면 IllgegalArgumentException이 발생한다.")
    void validateDuplicateUser() {
        User saveUser = user;
        User duplicateUser = duplicateIdUser;

        DataBase.addUser(saveUser);
        assertThrows(IllegalArgumentException.class, () -> DataBase.addUser(duplicateUser));
    }

    /**
     * 싱글톤 테스트로 계속해서 실패
     * 대안 찾기.
     */
    @Test
    @DisplayName("회원이 저장되면 전체 회원수가 증가해야 한다.")
    void add() {
        User saveUser = user;
        DataBase.addUser(saveUser);

        int expectedSize = 1;

        assertThat(expectedSize).isSameAs(DataBase.findAll().size());
    }

}
