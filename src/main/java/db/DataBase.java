package db;

import model.User;

import java.util.Collection;
import java.util.Map;
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
        if (userId == null) {
            return false;
        }

        User user;

        if ((user = findUserById(userId)) != null) {
            return user.hasPasswordEqualTo(password);
        }

        return false;
    }
}
