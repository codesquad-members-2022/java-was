package webserver;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.DataBase;
import model.User;

class UrlHandlerTest {

    @Test
    @DisplayName("회원가입 정보를 입력하면 User 객체를 생성하여 DB에 저장한다")
    void addUserInfo() {
        String url = "/user/create?userId=bckang&password=1234&name=얀&email=yan@naver.com";
        UrlHandler urlHandler = UrlHandler.getInstance();

        urlHandler.mapUrl(url);
        User user = DataBase.findUserById("bckang");
        assertThat(user.getName()).isEqualTo("얀");
    }
}
