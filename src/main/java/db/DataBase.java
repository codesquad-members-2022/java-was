package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import model.User;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static Optional<User> addUser(User user) {
        if (users.get(user.getUserId()) != null) {
            return Optional.empty();
        }
        users.put(user.getUserId(), user);
        return Optional.ofNullable(user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
