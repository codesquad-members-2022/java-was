package db;

import model.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {

    private DataBase() {

    }

    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        isValidUser(user);
        users.put(user.getUserId(), user);
    }

    private static void isValidUser(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }

    public static Optional<User> findUserById(String userId) {
        if (!users.containsKey(userId)) {
            return Optional.empty();
        }
        return Optional.of(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
