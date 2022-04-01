package model.handler;

import configuration.ObjectFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerFactoryTest {

    private ObjectFactory objectFactory;
    private HandlerFactory handlerFactory;

    @BeforeEach
    void init(){
        objectFactory = new ObjectFactory();
        handlerFactory = ObjectFactory.handlerFactory;
    }

    @Test
    @DisplayName("/index를 requestURL로 넣으면 Handler의 이름이 HomeController로 반환된다.")
    void findHomeController() {
        String requestURL = "/index";

        Handler homeController = HandlerFactory.getHandler(requestURL);
        String expectedName = "HomeController";

        Assertions.assertThat(homeController.getClass().getSimpleName()).isEqualTo(expectedName);
    }
}
