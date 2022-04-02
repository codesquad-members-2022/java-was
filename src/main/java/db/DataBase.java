package db;

import model.user.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class DataBase {

    private static String NO_SUCH_USER = "해당 사용자는 존재하지 않습니다.";
    private static String DUPLICATE_USER = "해당 아이디로 가입한 회원이 존재합니다.";
    private static List<User> users = new ArrayList<>();

    private DataBase() {
    }

    private static final DataBase instance = new DataBase();

    public static DataBase getInstance() {
        if (instance == null) {
            return new DataBase();
        }
        return instance;
    }

    public static void addUser(User user) {
        validateDuplicateId(user.getUserId());
        users.add(user);
    }

    private static void validateDuplicateId(String userId) {
        users.stream()
                .filter(user -> user.isSameId(userId))
                .findAny()
                .ifPresent(DataBase::duplicateId);
    }

    private static void duplicateId(User user) {
        throw new IllegalArgumentException(DUPLICATE_USER);
    }

    public static Collection<User> findAll() {
        return new ArrayList<>(users);
    }

    public static User login(String userId, String password) {
        return users.stream()
                .filter(user -> user.isSameId(userId))
                .filter(user -> user.isSamePassword(password))
                .findAny()
                .orElseGet(DataBase::duplicateUser);
    }

    private static User duplicateUser() {
        throw new IllegalArgumentException(NO_SUCH_USER);
    }

    public void clear() {
        users.clear();
    }
}
