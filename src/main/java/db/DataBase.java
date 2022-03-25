package db;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    public static final String ALREADY_REGISTER_USER_ERROR = "중복 회원 입니다.";
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        if (findUserById(user.getUserId()) == null) {
            users.put(user.getUserId(), user);
            return;
        }
        throw new IllegalArgumentException(ALREADY_REGISTER_USER_ERROR);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
