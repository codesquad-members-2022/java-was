package db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Maps;

import exception.DuplicatedUserException;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParser;

public class DataBase {
    private static final Logger log = LoggerFactory.getLogger(DataBase.class);
    public static final String ALREADY_REGISTER_USER_ERROR = "중복 회원 입니다.";
    private static Map<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        findUserById(user.getUserId())
                .ifPresent(u -> {
                    throw new DuplicatedUserException("ddd");
                });
        users.put(user.getUserId(), user);
    }

    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
