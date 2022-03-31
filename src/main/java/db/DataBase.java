package db;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private static final Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static boolean matchesExistingUser(String userId, String password) {
        return Optional.ofNullable(userId)
                .map(DataBase::findUserById)
                .map(user -> user.hasPasswordEqualTo(password))
                .orElse(false);
    }

    // 동작 확인을 위한 샘플 데이터
    static {
        addUser(new User("Sammy", "1234", "sammy", "sammy@naver.com"));
        addUser(new User("ikjo", "1234", "ikjo", "ikjo@naver.com"));
        addUser(new User("hounx", "1234", "hounx", "hounx@naver.com"));
    }
}
