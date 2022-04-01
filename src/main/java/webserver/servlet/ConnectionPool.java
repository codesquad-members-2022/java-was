package webserver.servlet;

import configuration.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static webserver.servlet.ServletStatus.RESPONSING;
import static webserver.servlet.ServletStatus.WAITING;

public class ConnectionPool {

    private Logger log = LoggerFactory.getLogger(ConnectionPool.class);
    private static final String NO_AVAILABLE_SERVLET = "현재 이용가능한 서블릿이 존재하지 않습니다.";

    private static final int MIN_POOL_SIZE = 0;
    private static final int MAX_POOL_SIZE = 200;
    private static final int WAITING_SECONDS = 5;

    private static RandomUtil randomUtil;
    private static final List<RequestWaitingServlet> servlets = new ArrayList<>(MAX_POOL_SIZE);
    private static final ConnectionPool instance = new ConnectionPool();

    private ConnectionPool (){
        randomUtil = ObjectFactory.randomUtil;
    };

    public static ConnectionPool getInstance() {
        if (instance == null) {
            return new ConnectionPool();
        }
        return instance;
    }

    static {
        for (int index = MIN_POOL_SIZE; index < MAX_POOL_SIZE; index++) {
            servlets.add(new RequestWaitingServlet(index));
        }
    }

    public static RequestWaitingServlet getServlet() {
        shuffleServlets();
        RequestWaitingServlet waitingServlet = findRequestWaitingServletWithParallel();
        if (waitingServlet.isAvailable()) {
            waitingServlet.changeServletStatus(RESPONSING);
        }
        return waitingServlet;
    }

    private static void shuffleServlets() {
        for (RequestWaitingServlet servlet : servlets) {
            if (randomUtil.isEvenNumber()) {
                servlet.changeServletStatus(WAITING);
            } else {
                servlet.changeServletStatus(RESPONSING);
            }
        }
    }

    private static RequestWaitingServlet findRequestWaitingServletWithParallel() {
        return servlets.stream().parallel()
                .filter(RequestWaitingServlet::isAvailable)
                .findAny()
                .orElseGet(getServletAfterWaiting());
    }

    private static Supplier<RequestWaitingServlet> getServletAfterWaiting() {
        try {
            TimeUnit.SECONDS.sleep(WAITING_SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ConnectionPool::getServlet;
    }

    public boolean hasAvailableServlet() {
        shuffleServlets();
        boolean availableServlet = findAvailableServlet();
        if (availableServlet) {
            return true;
        }
        log.info(NO_AVAILABLE_SERVLET);
        return false;
    }

    private boolean findAvailableServlet() {
        return servlets.stream()
                .anyMatch(RequestWaitingServlet::isAvailable);
    }
}
