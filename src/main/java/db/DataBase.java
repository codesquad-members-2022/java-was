package db;

import model.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class DataBase {

    private static String NO_SUCH_USER = "해당 사용자는 존재하지 않습니다.";
    private static List<User> users = new ArrayList<>();

    private DataBase() {}

    private static final DataBase instance = new DataBase();

    public static DataBase getInstance() {
        if (instance == null) {
            return new DataBase();
        }
        return instance;
    }

    public static void addUser(User user) {
        users.add(user);
    }

    public static void validateDuplicateId(String userId) {
        users.stream()
                .filter(user -> user.isSameId(userId))
                .findAny()
                .ifPresent(DataBase::duplicateId);
    }

    private static void duplicateId(User user) {
        throw new IllegalStateException(NO_SUCH_USER);
    }

    public static Collection<User> findAll() {
        return new ArrayList<>(users);
    }

    public static User login(String userId, String password) {
        return users.stream()
                .filter(user->user.isSameId(userId))
                .filter(user->user.isSamePassword(password))
                .findAny()
                .orElseGet(DataBase::duplicateUser);
    }

    private static User duplicateUser() {
        throw new IllegalStateException(NO_SUCH_USER);
    }
}
