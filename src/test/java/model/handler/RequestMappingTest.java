package model.handler;

import configuration.ObjectFactory;
import model.request.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

class RequestMappingTest {

    private HttpServletRequest httpServletRequest;
    private RequestMapping requestMapping;
    private ObjectFactory objectFactory;
    private HandlerFactory handlerFactory;

    @BeforeEach
    void init() {
        httpServletRequest = Mockito.mock(HttpServletRequest.class);
        objectFactory = new ObjectFactory();
        requestMapping = ObjectFactory.requestMapping;
        handlerFactory = ObjectFactory.handlerFactory;
    }

    @Test
    @DisplayName("/index를 requestURL로 넣으면 Handler의 이름이 HomeController로 반환된다.")
    void findHomeController() {
        when(httpServletRequest.getRequestURL()).thenReturn("/index");
        httpServletRequest.getRequestURL();

        Handler homeController = requestMapping.getHandler(httpServletRequest);
        String expectedName = "HomeController";

        Assertions.assertThat(homeController.getClass().getSimpleName()).isEqualTo(expectedName);
    }
}
