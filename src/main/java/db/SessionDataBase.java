package db;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDataBase {

    /**
     * Key - UUID.tostring
     * Value - User.UserId
     */
    private static Map<String, String> store = new ConcurrentHashMap();

    public static String addSession(String userId) {
        String uuid = UUID.randomUUID().toString();
        store.put(uuid, userId);

        return uuid;
    }

    public static Optional<String> findUserIdByCookie(String cookie) {
        String userId = store.get(cookie);
        return Optional.ofNullable(userId);
    }

    public static boolean deleteByCookie(String cookie) {
        String userId = store.remove(cookie);

        if ("".equals(userId)) {
            return false;
        }
        return true;
    }

}
