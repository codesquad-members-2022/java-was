package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;
import java.util.UUID;

public class SessionDataBase {

    private static final Map<String, User> sessions = Maps.newHashMap();

    // TODO Response Set-cookie 헤더에 sessionId 를 담아서 보내주는 작업
    // 세션 저장
    public static String saveSession(User user) {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, user);
        return sessionId;
    }

    // 세션 체크 (로그인 사용자, 게스트 구분 등에 사용 ?)
    public static boolean findSessionByUser(String sessionId) {
        return sessions.get(sessionId) != null;
    }

    public static void remove(String sessionId) {
        sessions.remove(sessionId);
    }
}
