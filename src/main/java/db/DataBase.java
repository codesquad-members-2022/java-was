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
}
