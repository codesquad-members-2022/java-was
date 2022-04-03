package webserver.servlet;

import configuration.ObjectFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ConnectionPoolTest {

    private ConnectionPool connectionPool;

    @BeforeEach
    void init() {
        connectionPool = ObjectFactory.connectionPool;
    }

//    @Test
//    @DisplayName("이용가능한 서블릿은 상태가 응답중(RESPONSING)이어야 한다.")
//    void getServlet() {
//        RequestWaitingServlet servlet = ConnectionPool.getServlet();
//
//        assertThat(servlet.getServletStatus()).isEqualTo(ServletStatus.RESPONSING);
//    }
}
