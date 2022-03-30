package db;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.Cookie;

import java.util.Map;
import java.util.UUID;

public class SessionDataBase {

    private static final Logger log = LoggerFactory.getLogger(SessionDataBase.class);

    private SessionDataBase() {
    }

    private static Map<String, Cookie> session = Maps.newHashMap();

    public static String addCookie(Cookie cookie) {
        String sessionId = UUID.randomUUID().toString();
        session.put(sessionId, cookie);
        return sessionId;
    }

    public static Cookie findByUUID(String StringUUID) {
        return session.get(StringUUID);
    }

    public static void expire(String sessionId) {
        log.debug("sessionId 1 {}",session.toString());
        session.remove(sessionId);
        log.debug("sessionId 2 {}",session.toString());
    }

    public static boolean isLoginUser(String userId) {
        return session.containsKey(userId);
    }

}
