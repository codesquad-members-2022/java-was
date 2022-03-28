package db;

import model.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataBase {
    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.isSameId(userId))
                .findAny()
                .orElseThrow();
    }

    public static Collection<User> findAll() {
        return new ArrayList<>(users);
    }
}
