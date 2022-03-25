package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("중복된 회원 아이디 입니다.");
        }
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
