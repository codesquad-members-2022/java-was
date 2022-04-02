package model.handler;

import configuration.ObjectFactory;
import model.handler.controller.HandlerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HandlerFactoryTest {

    private HandlerFactory handlerFactory;

    @BeforeEach
    void init() {
        handlerFactory = ObjectFactory.handlerFactory;
    }

    @Test
    @DisplayName("requestURL로 없는 값이 들어오면 HomeController가 반환된다.")
    void getHomeControllerIfNull() {
        String requestURL = "/index/codesquad";

        Handler homeController = HandlerFactory.getHandler(requestURL);
        String expectedName = "HomeController";

        assertThat(homeController.getClass().getSimpleName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("requestURL로 /index를 넣으면 Handler의 이름이 HomeController로 반환된다.")
    void findHomeController() {
        String requestURL = "/index";

        Handler homeController = HandlerFactory.getHandler(requestURL);
        String expectedName = "HomeController";

        assertThat(homeController.getClass().getSimpleName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("requestURL로 /user/form을 넣으면 Handler의 이름이 UserCreateController가 반환된다.")
    void findUserCreateController() {
        String requestURL = "/user/form";

        Handler userCreateController = HandlerFactory.getHandler(requestURL);
        String expectedName = "UserCreateController";

        assertThat(userCreateController.getClass().getSimpleName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("requestURL로 /user/login 넣으면 Handler의 이름이 UserLoginController가 반환된다.")
    void findUserLoginController() {
        String requestURL = "/user/login";

        Handler userLoginController = HandlerFactory.getHandler(requestURL);
        String expectedName = "UserLoginController";

        assertThat(userLoginController.getClass().getSimpleName()).isEqualTo(expectedName);
    }
}
