package db;

import model.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class DataBase {

    private static String NO_SUCH_USER = "해당 사용자는 존재하지 않습니다.";
    private static List<User> users = new ArrayList<>();

    public static void addUser(User user) {
        users.add(user);
    }

    public static User findUserById(String userId) {
        return users.stream()
                .filter(user -> user.isSameId(userId))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NO_SUCH_USER));
    }

    public static Collection<User> findAll() {
        return new ArrayList<>(users);
    }
}
