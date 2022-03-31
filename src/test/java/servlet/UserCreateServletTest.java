package servlet;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import db.DataBase;
import http.Request;
import http.Response;
import model.User;

class UserCreateServletTest {

    private UserCreateServlet userCreateServlet;
    private User user;
    private Request request;
    private Response response;
    private Map<String, String> queryParameter = new HashMap<>();

    @BeforeEach
    void setUp() {
        userCreateServlet = new UserCreateServlet();
        user = new User("user1", "1111", "name1", "email1");

        queryParameter.put("userId", "user1");
        queryParameter.put("password", "1111");
        queryParameter.put("name", "name1");
        queryParameter.put("email", "email1");

        request = new Request();
        response = new Response();
        request.addParameters(queryParameter);
    }

    @DisplayName("중복되는 id가 존재하면, form.html로 redirect 경로가 지정된다.")
    @Test
    void id_exist_redirect_form() {
        // given
        DataBase.addUser(user);

        // when
        Response responseResult = userCreateServlet.doPost(request, response);

        // then
        assertThat(responseResult.getRedirectUrl()).isEqualTo("/user/form.html");
    }

    @DisplayName("새로운 id라면 데이터베이스에 저장된 후 /index.html 경로가 지정된다.")
    @Test
    void id_new_index_form() {
        // when
        Response responseResult = userCreateServlet.doPost(request, this.response);

        // then
        User findUser = DataBase.findByUserId("user1");
        assertThat(responseResult.getRedirectUrl()).isEqualTo("/index.html");
        assertThat(findUser.getUserId()).isEqualTo("user1");
    }

    @AfterEach
    void tearDown() {
        DataBase.deleteAll();
    }
}
