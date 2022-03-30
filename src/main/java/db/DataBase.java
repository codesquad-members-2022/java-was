package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Collection;
import java.util.Map;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        if (hasDuplicateId(user)) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다");
        }
        users.put(user.getUserId(), user);
    }

    public static boolean hasDuplicateId(User user) {
        return users.keySet().stream()
                .anyMatch(id -> user.getUserId().equals(id));
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
