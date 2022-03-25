package db;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = new HashMap<>();

    public static void addUser(User user) {
        checkDuplicate(user.getUserId());
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    private static void checkDuplicate(String userId) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("중복된 userId입니다.");
        }
    }

}
