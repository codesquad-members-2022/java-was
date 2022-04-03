package model.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserTest {


    private User user;
    private ArgumentCaptor<String> stringArgumentCaptor;

    private User getUser() {
        return new User("Min", "1234", "Min", "min@gmail.com");
    }

    @BeforeEach
    void init() {
        stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
    }

    @Test
    @DisplayName("")
    void m() throws Exception {
        user = getUser();

        String expectedId = "Min";
        String expectedPassword = "1234";

        assertThat(expectedId).isEqualTo(user.getUserId());
        assertThat(expectedPassword).isEqualTo(user.getPassword());
    }

}
