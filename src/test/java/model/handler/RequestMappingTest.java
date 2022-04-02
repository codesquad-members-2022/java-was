package model.handler;

import configuration.ObjectFactory;
import model.handler.controller.HandlerFactory;
import model.handler.controller.RequestMapping;
import model.http.request.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("RequestMapping")
class RequestMappingTest {

    private HttpServletRequest httpServletRequest;
    private RequestMapping requestMapping;
    private HandlerFactory handlerFactory;

    @BeforeEach
    void init() {
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        requestMapping = ObjectFactory.requestMapping;
        handlerFactory = ObjectFactory.handlerFactory;
    }

    @Test
    @DisplayName("requestURL로 /index를 넣으면 Handler의 이름이 HomeController인 컨트롤러가 반환된다.")
    void findHomeController() {
        when(httpServletRequest.getRequestURL()).thenReturn("/index");
        httpServletRequest.getRequestURL();

        Handler homeController = requestMapping.getHandler(httpServletRequest);
        String expectedName = "HomeController";

        assertThat(homeController.getClass().getSimpleName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("requestURL로 /user/form을 넣으면 Handler의 이름이 UserCreateController인 컨트롤러가 반환된다.")
    void findUserCreateController() {
        when(httpServletRequest.getRequestURL()).thenReturn("/user/form");
        httpServletRequest.getRequestURL();

        Handler userCreateController = requestMapping.getHandler(httpServletRequest);
        String expectedName = "UserCreateController";

        assertThat(userCreateController.getClass().getSimpleName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("requestURL로 /user/login 넣으면 Handler의 이름이 UserLoginController인 컨트롤러가 반환된다.")
    void findUserLoginController() {
        when(httpServletRequest.getRequestURL()).thenReturn("/user/login");
        httpServletRequest.getRequestURL();

        Handler userLoginController = requestMapping.getHandler(httpServletRequest);
        String expectedName = "UserLoginController";

        assertThat(userLoginController.getClass().getSimpleName()).isEqualTo(expectedName);
    }
}
